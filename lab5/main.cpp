#include <iostream>
#include <vector>
#include <thread>
#include <algorithm>
#include <iomanip>
#include <functional>
#include <chrono>
#include <random>
#include <numeric>
#include <omp.h>


using time_val = std::chrono::microseconds;
const std::string time_val_str{"microseconds"};

constexpr unsigned JOBS_COUNT = 3;
constexpr unsigned MATRIX_SIZE = 1000;


template <class T, class ChronoTimeType>
std::pair<T, ChronoTimeType> measureTime( const std::function<T()>& f ) {
	auto t1 = std::chrono::high_resolution_clock::now();
	T res = f();
	auto t2 = std::chrono::high_resolution_clock::now();

	return std::make_pair( std::move( res ), std::chrono::duration_cast<ChronoTimeType>( t2 - t1 ) );
}

template <class T>
std::ostream& operator <<( std::ostream& out, const std::vector<T>& vec ) {
	out << "[" << *vec.begin();
	std::for_each( std::next( vec.begin() ), vec.end(), [&out]( const T& item ) { out << ", " << item; } );
	return out << "]";
}

double* rnd_array(int size) {
        double* a = new double[size];
        std::random_device rd;
        std::default_random_engine dre(rd());
        std::uniform_real_distribution<double> uid(0.0, 1.0);

        std::generate(a, a + size, [&] () { return uid(dre); });
        return a;
}

std::vector<double> serial( std::vector<std::vector<double>> M, std::vector<double> b ) {
	const int size = M.size();
	for (int i = 0; i < size - 1; i++) {
		for (int j = i + 1; j < size; j++) {
			double k = -M[j][i] / M[i][i];
			b[j] += b[i] * k;

			for (int l = i; l < size; l++) {
				M[j][l] += M[i][l] * k;
			}
		}
	}

	b[size - 1] /= M[size -1][size - 1];
	for (int i = size - 2; i >= 0; i--) {
		double sum = 0;
		for (int j = i + 1; j < size; j++) {
			sum += M[i][j] * b[j];
		}
		b[i] = (b[i] - sum) / M[i][i];
	}

	return b;
}

std::vector<double> parallel( std::vector<std::vector<double>> M, std::vector<double> b ) {
	const int size = M.size();
	double k;
	int i, j, l;

	for (i = 0; i < size - 1; i++) {
#pragma omp parallel for shared(M, b) private(k, j, l)
		for (j = i + 1; j < size; j++) {
			k = -M[j][i] / M[i][i];
			b[j] += b[i] * k;

#pragma omp simd
			for (l = i; l < size; l++) {
				M[j][l] += M[i][l] * k;
			}
		}
	}

	b[size - 1] /= M[size - 1][size - 1];
	for (i = size - 2; i >= 0; i--) {
		double sum = 0;

#pragma omp simd
		for (j = i + 1; j < size; j++) {
			sum += M[i][j] * b[j];
		}
		b[i] = (b[i] - sum) / M[i][i];
	}
	return b;
}


int main() {

	std::vector<std::vector<double>> matrix = {
		{3, 2, -4},
		{2, 3, 3},
		{5, -3, 1}
	};

	std::vector<double> B = {3, 15, 14};

	const unsigned N = matrix.size();
	if (N <= 1 || matrix[0].size() != N) {
		return 1;
	}

	const auto res_serial = measureTime<std::vector<double>, time_val>(
		[&matrix, &B]() {
			return serial( matrix, B );
		}
	);
	std::cout << "SERIAL: " << res_serial.first << std::endl;
	std::cout << "Time taken: " << res_serial.second.count() << " " << time_val_str << std::endl;
	
	const auto res_parallel = measureTime<std::vector<double>, time_val>(
		[&matrix, &B]() {
			return parallel( matrix, B );
		}
	);
	std::cout << "PARALLEL: " << res_parallel.first << std::endl;
	std::cout << "Time taken: " << res_parallel.second.count() << " " << time_val_str << std::endl;


	std::cout << std::endl << "RANDOM MATRIX" << std::endl;

	std::vector<std::vector<double>> rnd_matrix(MATRIX_SIZE);
	for(auto& row : rnd_matrix) {
		double* data = rnd_array( MATRIX_SIZE );
		row = std::vector<double>(data, data + MATRIX_SIZE);
	}
	double* b_data = rnd_array( MATRIX_SIZE );
	std::vector<double> rnd_B(b_data, b_data + MATRIX_SIZE);

	const auto res_rnd_serial = measureTime<std::vector<double>, time_val>(
		[&rnd_matrix, &rnd_B]() {
			return serial( rnd_matrix, rnd_B );
		}
	);
	std::cout << "SERIAL (only first X): " << res_rnd_serial.first[0] << std::endl;
	std::cout << "Time taken: " << res_rnd_serial.second.count() << " " << time_val_str << std::endl;
	
	const auto res_rnd_parallel = measureTime<std::vector<double>, time_val>(
		[&rnd_matrix, &rnd_B]() {
			return parallel( rnd_matrix, rnd_B );
		}
	);
	std::cout << "PARALLEL (only first X): " << res_rnd_parallel.first[0] << std::endl;
	std::cout << "Time taken: " << res_rnd_parallel.second.count() << " " << time_val_str << std::endl;

}
