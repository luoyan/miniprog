#include <iostream>
#include <fstream>
#include <vector>
#include <ext/hash_map>
#include <map>
#include <set>

#include "XTrie.h"

using namespace std;
using namespace __gnu_cxx;
using namespace b2bmlr;

struct hash_vector
{
	size_t operator()(const vector<int> &v) const
	{
		static const uint32_t primes[16] = {
			0x01EE5DB9, 0x491408C3, 0x0465FB69, 0x421F0141,
			0x2E7D036B, 0x2D41C7B9, 0x58C0EF0D, 0x7B15A53B,
			0x7C9D3761, 0x5ABB9B0B, 0x24109367, 0x5A5B741F,
			0x6B9F12E9, 0x71BA7809, 0x081F69CD, 0x4D9B740B,
		};
		uint32_t sum = 0;
		for (size_t i = 0; i < v.size(); i ++)
			sum += primes[i & 15] * (uint32_t)v[i];
		return sum;
	}
};

int main(int argc, char **argv)
{
	hash_map<vector<int>, set<int>, hash_vector> hidingwords;

	string line;
	while (getline(cin, line))
	{
		vector<vector<int> > wordids;
		size_t t = 0;
		while (t < line.size())
		{
			size_t t2 = line.find('\t', t);
			if (t2 == string::npos) t2 = line.size();
			vector<int> ids;
			size_t s = t;
			while (s < t2)
			{
				size_t s2 = line.find(' ', s);
				if (s2 == string::npos || s2 > t2) s2 = t2;
				if (s2 > s) ids.push_back(atoi(line.substr(s, s2 - s).c_str()));
				s = s2 + 1;
			}
			if (!ids.empty()) wordids.push_back(ids);
			t = t2 + 1;
		}

		if (wordids.size() == 2
				&& wordids[0].size() >= 1 && wordids[1].size() >= 1)
			for (size_t i = 0; i < wordids[1].size(); i ++)
				hidingwords[wordids[0]].insert(wordids[1][i]);
	}

	XTrie dict;
	for (hash_map<vector<int>, set<int>, hash_vector>::const_iterator
			it = hidingwords.begin(); it != hidingwords.end(); ++ it)
	{
		vector<int> ids(it->second.begin(), it->second.end());
		dict.Add((void *)(&(it->first[0])), sizeof(int) * it->first.size(),
				(void *)(&ids[0]), sizeof(int) * ids.size());
		cout << it->first[0];
		for (size_t i = 1; i < it->first.size(); i ++)
			cout << " " << it->first[i];
		cout << "\t" << ids[0];
		for (size_t i = 1; i < ids.size(); i ++)
			cout << " " << ids[i];
		cout << endl;
	}
	dict.Save(argv[1]);

	return 0;
}
