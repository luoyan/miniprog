#include <iostream>
#include <cstdio>
struct Node;
struct Node
{
    int nData;
    Node * pNext;
};
typedef Node* List;
List remove(List head, int k)
{
    if (k < 0 || head == NULL)
        return NULL;
    if (k == 0)
    {
        if (head->pNext)
        {
            Node * pNode = head;
            head = head->pNext;
            delete pNode;
            return head;
        }
        else
            return NULL;
    }
    Node * pPrev = NULL;
    Node * pNode = head;
    for (int i = 0; i < k; i++)
    {
        pPrev = pNode;
        pNode = pNode->pNext;
        if (pNode == NULL)
            return NULL;
    }
    pPrev = pNode->pNext;
    delete pNode;
    return head;
}
void show_list(List head)
{
    int i = 0;
    for (Node * pNode = head; pNode;pNode = pNode->pNext, i++)
    {
        printf("node[%d] %d\n", i, pNode->nData);
    }
}
void test_remove()
{
    Node * pNodeArray[4];
    for (int i = 0; i < 4;i++)
    {
        pNodeArray[i] = new Node;
    }
    pNodeArray[0]->nData = 4;
    pNodeArray[0]->pNext = pNodeArray[1];
    pNodeArray[1]->nData = 5;
    pNodeArray[1]->pNext = pNodeArray[2];
    pNodeArray[2]->nData = 2;
    pNodeArray[2]->pNext = pNodeArray[3];
    pNodeArray[3]->nData = 3;
    pNodeArray[3]->pNext = NULL;
    int test_case[] = {-1, 6, 4, 3, 0};
    Node * expect[] = {NULL, NULL, NULL, pNodeArray[0], pNodeArray[1]};
    List head = pNodeArray[0];
    for (int i = 0; i < sizeof(test_case)/sizeof(int); i++)
    {
        List res = remove(head, test_case[i]);
        if (res)
            head = res;
        if (res == expect[i])
            printf("PASS case %d\n", i);
        else
            printf("Failed case %d %p != %p\n", i, res, expect[i]);
    }
}
int main()
{
    test_remove();
}
