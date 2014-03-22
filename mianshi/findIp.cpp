#include <iostream>
#include <cstdio>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <cstring>

typedef uint32_t IPADDRESS;
IPADDRESS ipstr2uint(char *ip, bool & success)
{
    uint32_t ip_int = 0;
    uint32_t sum = 0;
    for (int i = 0;ip[i];i++) {
       if (ip[i] <= '9' && ip[i] >= '0')
       {
           sum = sum * 10 + ip[i] - '0';
           if (sum >255)
           {
               printf("failed to transform str to ip %s sum %d i %d\n", ip, sum, i);
               success = false;
               return 0;
           }
       }
       else if (ip[i] == '.')
       {
           ip_int = ip_int * 256 + sum;
           sum = 0;
       }
       else {
           printf("failed to transform str to ip %s char [%c]\n", ip, ip[i]);
           success = false;
           return 0;
       }
    }
    ip_int = ip_int * 256 + sum;
    return ip_int;
}
struct IpRange{
    IPADDRESS nStart;
    IPADDRESS nEnd;
    char * pStartIp;
    char * pEndIp;
    char * pName;
};
int binarySearch(IpRange a[], IPADDRESS N, int left, int right)
{
    int mid = (left + right) / 2;
    for (;mid >= left && mid <= right;)
    {
        if (N < a[mid].nStart)
        {
            right = mid - 1;
        }
        else if (N >= a[mid].nStart && N <= a[mid].nEnd)
        {
            return mid;
        }
        else if (N > a[mid].nEnd && ((mid < right && N < a[mid + 1].nStart) || (mid == right)))
        {
            printf("not hit any ip range\n");
            return -1;
        }
        else
        {
            left = mid + 1;
        }
        mid = (left + right) / 2;
    }
    return -1;
}

void testBinarySearch()
{
    IpRange ip_range_array[] = 
    {
        {0, 0, "10.100.100.1", "10.100.100.255", "shanghai"},
        {0, 0, "10.100.200.1", "10.100.200.255", "hangzhou"},
        {0, 0, "10.100.210.1", "10.100.210.255", "beijing"},
    };
    for (int i = 0; i < sizeof(ip_range_array)/sizeof(IpRange);i++)
    {
        bool success = true;
        ip_range_array[i].nStart = ipstr2uint(ip_range_array[i].pStartIp, success);
        ip_range_array[i].nEnd = ipstr2uint(ip_range_array[i].pEndIp, success);
        printf("ip [%s - %s] [%lu - %lu]\n", ip_range_array[i].pStartIp, ip_range_array[i].pEndIp, ip_range_array[i].nStart, ip_range_array[i].nEnd);
    }
    char * ip_str_array[] = {
        "10.100.200.10",
        "10.100.210.1",
        "10.200.205.1",
    };
    char * expect[] = {
        "hangzhou",
        "beijing",
        "out of range",
    };
    for (int i = 0; i < sizeof(ip_str_array)/sizeof(char *);i++)
    {
        bool success = true;
        IPADDRESS ip = ipstr2uint(ip_str_array[i], success);
        printf("ip %lu\n", ip);
        int index = binarySearch(ip_range_array, ip, 0, (sizeof(ip_range_array)/sizeof(IpRange)) - 1);
        char * res = NULL;
        if (index < 0)
            res = "out of range";
        else
            res = ip_range_array[index].pName;
        if (strcmp(res, expect[i]) == 0)
            printf("PASS case %d\n", i);
        else {
            printf("failed case %d %s != %s\n", i, res, expect[i]);
        }
    }
}
int main()
{
    testBinarySearch();
}
