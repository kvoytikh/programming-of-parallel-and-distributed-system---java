#include <vector>
#include <thread>
#include <iostream>

void threadFunc(const int* arr, int start, int end,
                int& result)
{
    result = 0;
    for (int i = start; i < end; i++)
    {
        result += arr[i];
    }
}

int main()
{
    std::vector<int> arr(1000, 5);

    int numCores = 10;

    std::vector<std::thread> threadList;
    std::vector<int> resultArr(numCores);

    for (int i = 0; i < numCores; i++)
    {
        int begin = 0;
        int end = 100;
        threadList.push_back(std::thread(
                                 threadFunc,
                                 arr.data(),
                                 begin,
                                 end,
                                 std::ref(resultArr[i])
                                 ));

    }

    int sum = 0;
    for (int i = 0; i < numCores; i++)
    {
        threadList[i].join();
        sum += resultArr[i];

    }

    std::cout << "sum" << sum << std::endl;




}

