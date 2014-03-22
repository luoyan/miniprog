#include <iostream>
#include <cstdio>
class TreeNode;
class Visitor
{
public:
    Visitor(int k);
    bool visit(TreeNode * pNode);
    TreeNode * getNode() {return m_pNode;};
private:
    bool m_bEnd;
    int m_nCount;
    int m_nK;
    TreeNode * m_pNode;
};
class TreeNode
{
public:
    TreeNode(int nData, TreeNode * pLeft, TreeNode * pRight)
    {
        this->m_pLeft = pLeft;
        this->m_pRight = pRight;
        this->m_nData = nData;
    }
    bool tranverseReverseInorder(Visitor & v)
    {
        if (this->m_pRight)
        {
            if (!this->m_pRight->tranverseReverseInorder(v))
                return false;
        }
        if (!v.visit(this))
        {
            return false;
        }
        if (this->m_pLeft)
        {
            if (!this->m_pLeft->tranverseReverseInorder(v))
                return false;
        }
        return true;
    }
public:
    int getData() {
        return m_nData;
    }
private:
    TreeNode * m_pLeft;
    TreeNode * m_pRight;
    int m_nData;
};
Visitor::Visitor(int k)
{
    m_nK = k;
    m_bEnd = false;
    m_pNode = NULL;
    m_nCount = 0;
    if (k < 0)
    {
        m_bEnd = true;
    }
}
bool Visitor::visit(TreeNode *pNode)
{
    if (!m_bEnd)
    {
        if (m_nK == m_nCount)
        {
            m_bEnd = true;
            m_pNode = pNode;
        }
        m_nCount++;
    }
    return !m_bEnd;
}
/*

          100(7)
       /      \
    80(6)          120(5)
  /   \        /  \
 65(4) 85(3) 110(2) 130(1)
 */
void test_tranverseReverseInorder()
{
    TreeNode node1(130, NULL, NULL);
    TreeNode node2(110, NULL, NULL);
    TreeNode node3(85, NULL, NULL);
    TreeNode node4(65, NULL, NULL);
    TreeNode node5(120, &node2, &node1);
    TreeNode node6(80, &node4, &node3);
    TreeNode node7(100, &node6, &node5);
    int k_array[] = {-1, 0, 1, 2, 3, 4, 5, 6, 7};
    int expect[] = {-1, 130, 120, 110, 100, 85, 80, 65, -1};
    for (int i = 0; i < sizeof(k_array)/sizeof(int);i++)
    {
        Visitor v(k_array[i]);
        node7.tranverseReverseInorder(v);
        int res = -1;
        if (v.getNode())
        {
            int nData = v.getNode()->getData();
            printf("success to find %d element %d\n", k_array[i], nData);
            res = nData;
        }
        else
        {
            printf("failed to find %d element\n", k_array[i]);
        }
        if (res == expect[i])
        {
            printf("PASS case %d\n", i);
        }
        else
        {
            printf("failed case %d %d != %d\n", i, res, expect[i]);
        }

    }
}
int main()
{
    test_tranverseReverseInorder();
}
