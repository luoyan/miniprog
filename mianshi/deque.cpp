#include <iostream>
#include <cstdio>
class Deque
{
public:
    Deque(int size = 16)
    {
        m_nStart = 0;
        m_nEnd = 0;
        m_nLength = size;
        m_pData = new int[m_nLength];
        printf("new %d %p\n", m_nLength, m_pData);
    };
    void push_back(int n)
    {
        if (is_full())
            expand();
        m_pData[m_nEnd] = n;
        m_nEnd = (m_nEnd + 1) % m_nLength;
    };
    void push_front(int n)
    {
        if (is_full())
            expand();
        m_nStart = (m_nStart - 1 + m_nLength) % m_nLength;
        m_pData[m_nStart] = n;
    };
    int pop_back()
    {
        if (is_empty())
            return -1;
        m_nEnd = (m_nEnd - 1 + m_nLength) % m_nLength;
        return m_pData[m_nEnd];
    };
    int pop_front()
    {
        if (is_empty())
            return -1;
        int n = m_pData[m_nStart];
        m_nStart = (m_nStart + 1) % m_nLength;
        return n;
    };
    void expand()
    {
        int *pData = new int[m_nLength * 2];
        printf("expand new %d %p\n", m_nLength * 2, pData);
        int old_size = size();
        for (int i = 0; i < old_size;i++)
        {
            pData[i] = get(i);
        }
        delete m_pData;
        printf("expend delete %p\n", m_pData);
        m_pData = pData;
        m_nLength *= 2;
        m_nStart = 0;
        m_nEnd = old_size;
    };
    int get(int i)
    {
        if (i < 0 || i >= size())
            return -1;
        int index = (m_nStart + i) % m_nLength;
        return m_pData[index];
    };
    int size()
    {
        return (m_nEnd - m_nStart + m_nLength) % m_nLength;
    };
    bool is_full()
    {
        return ((m_nStart - m_nEnd) % m_nLength) == 1;
    };
    bool is_empty()
    {
        return m_nEnd == m_nStart;
    };
    ~Deque()
    {
        printf("destruct delete %p\n", m_pData);
        delete m_pData;
    }
private:
    int * m_pData;
    int m_nLength;
    int m_nStart;
    int m_nEnd;
};
void test_Deque()
{
    Deque dq(2);
    dq.push_front(1);
    dq.push_front(2);
    dq.push_back(3);
    dq.push_back(4);
    int expect[] = {
        2, 1, 3, 4
    };
    for (int i = 0;i < 4;i++)
    {
        if (dq.get(i) == expect[i])
        {
            printf("PASS case %d\n", i);
        }
        else
        {
            printf("Failed to case %d %d != %d\n", i, dq.get(i), expect[i]);
        }
    }
    int actual[4];
    actual[0] = dq.pop_back();
    actual[1] = dq.pop_back();
    actual[2] = dq.pop_front();
    actual[3] = dq.pop_front();
    int expect2[] = {
        4, 3, 2, 1
    };
    for (int i = 0; i < 4; i++)
    {
        if (actual[i] == expect2[i])
        {
            printf("PASS case %d\n", i);
        }
        else
        {
            printf("Failed to case %d %d != %d\n", i, actual[i], expect2[i]);
        }
    }
}
int main()
{
    test_Deque();
}
