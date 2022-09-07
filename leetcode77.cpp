#include <bits/stdc++.h>
using namespace std;

class Solution
{
public:
    void comb(vector<vector<int>> &ans, int a, int n, int k, vector<int> &v)
    {
        if (v.size() == k)
        {
            ans.push_back(v);
            return;
        }

        for (int i = a; i <= n; i++)
        {
            v.push_back(i);
            comb(ans, i + 1, n, k, v);
            v.pop_back();
        }
    }

    vector<vector<int>> combine(int n, int k)
    {
        vector<vector<int>> ans;
        vector<int> v;
        for (int i = 1; i <= n - k + 1; i++)
        {
            v.push_back(i);
            comb(ans, i + 1, n, k, v);
            v.pop_back();
        }

        return ans;
    }
};