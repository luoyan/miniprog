#include <iostream>
#include <fstream>
#include <map>
#include <set>

#include "XTrie.h"

using namespace std;
using namespace b2bmlr;

struct Pair
{
	int cat;
	float ratio;
};

int main(int argc, char **argv)
{
	XTrie dict;

	if (argc != 3)
	{
		cout << "./XTrieBuildClickcount ClickcountAmend.seg OutputDictName < InputDictName.seg" << endl;
		return -1;
	}

	map<vector<int>, set<int> > mapClickcountAmend;

	ifstream ifClickcountAmend(argv[1]);
	string line;
	while (getline(ifClickcountAmend, line))
	{
		size_t t = line.find('\t');
		if (t == string::npos) continue;
		string word = line.substr(0, t);
		string data = line.substr(t + 1);

		vector<int> wordids;
		for (t = 0; t < word.size(); )
		{
			size_t t2 = word.find(' ', t);
			if (t2 == string::npos) t2 = word.size();
			if (t2 > t)
				wordids.push_back(atoi(word.substr(t, t2 - t).c_str()));
			t = t2 + 1;
		}
		
		int cat = atoi(data.c_str());

		if (!wordids.empty())
		{
			if (cat == 0)
			{
				mapClickcountAmend[wordids] = set<int>();
			}
			else
			{
				map<vector<int>, set<int> >::iterator itAmend =
						mapClickcountAmend.find(wordids);
				if (itAmend == mapClickcountAmend.end() || !itAmend->second.empty())
					mapClickcountAmend[wordids].insert(cat);
			}
		}
	}
	ifClickcountAmend.close();

	while (getline(cin, line))
	{
		size_t t = line.find('\t');
		if (t == string::npos) continue;
		string word = line.substr(0, t);
		string data = line.substr(t + 1);

		vector<int> wordids;
		for (t = 0; t < word.size(); )
		{
			size_t t2 = word.find(' ', t);
			if (t2 == string::npos) t2 = word.size();
			wordids.push_back(atoi(word.substr(t, t2 - t).c_str()));
			t = t2 + 1;
		}

		map<vector<int>, set<int> >::iterator itAmend =
				mapClickcountAmend.find(wordids);
		if (itAmend != mapClickcountAmend.end() && itAmend->second.empty())
			continue;

		vector<Pair> catratios;
		for (t = 0; t < data.size(); )
		{
			size_t t2 = data.find(' ', t);
			if (t2 == string::npos) break; // t2 = data.size();
			size_t t3 = data.find(' ', t2 + 1);
			if (t3 == string::npos) t3 = data.size();
			Pair p;
			p.cat = atoi(data.substr(t, t2 - t).c_str());
			p.ratio = atof(data.substr(t2 + 1, t3 - t2 - 1).c_str());
			if (itAmend != mapClickcountAmend.end()
					&& itAmend->second.find(p.cat) != itAmend->second.end())
				p.ratio = 1.0;
			catratios.push_back(p);
			t = t3 + 1;
		}

		if (!wordids.empty() && !catratios.empty())
			dict.Add((void *)(&wordids[0]), sizeof(int) * wordids.size(),
					(void *)(&catratios[0]), sizeof(Pair) * catratios.size());
	}
	dict.Save(argv[2]);

	return 0;
}

/* vim: set ts=2 sw=2 noet : */
