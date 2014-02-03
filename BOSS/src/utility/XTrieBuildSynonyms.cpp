#include <iostream>
#include <fstream>
#include <vector>
#include <map>
#include <set>

#include "XTrie.h"

using namespace std;
using namespace b2bmlr;

int main(int argc, char **argv)
{
	map<int, set<int> > synonyms;
	string dictname;

	size_t a = 0;
	for (a = 1; a < (size_t)argc; a += 2)
	{
		if (a == (size_t)argc - 1
				|| string(argv[a]) != "-s"
				&& string(argv[a]) != "-b"
				&& string(argv[a]) != "-p"
				&& string(argv[a]) != "-o")
			break;
		else if (string(argv[a]) == "-o")
		{
			dictname = argv[a + 1];
			continue;
		}

		int type =
				(string(argv[a]) == "-s") ? 1 : (
				(string(argv[a]) == "-b") ? 2 : (
				(string(argv[a]) == "-p") ? 3 : (0)));

		ifstream file(argv[a + 1]);
		string line;
		while (getline(file, line))
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

			set<pair<int, int> > setPairsNotAdded;
			for (size_t i = 0; i < wordids.size(); i ++)
				for (size_t j = 0; j < wordids[i].size(); j ++)
					for (size_t k = j + 1; k < wordids[i].size(); k ++)
						if (wordids[i][j] <= wordids[i][k])
							setPairsNotAdded.insert(pair<int, int>(wordids[i][j], wordids[i][k]));
						else
							setPairsNotAdded.insert(pair<int, int>(wordids[i][k], wordids[i][j]));

			if (type == 1)
			{
				while (wordids.size() >= 2)
				{
					for (size_t i = 0; i < wordids.size() - 1; i ++)
						for (size_t j = 0; j < wordids[i].size(); j ++)
							for (size_t k = 0; k < wordids.back().size(); k ++)
								if (wordids[i][j] != wordids.back()[k]
										&& setPairsNotAdded.find(pair<int, int>(
											wordids[i][j], wordids.back()[k])) == setPairsNotAdded.end()
										&& setPairsNotAdded.find(pair<int, int>(
											wordids.back()[k], wordids[i][j])) == setPairsNotAdded.end())
								{
									synonyms[wordids[i][j]].insert(wordids.back()[k]);
									synonyms[wordids.back()[k]].insert(wordids[i][j]);
								}
					wordids.pop_back();
				}
			}
			else if (type == 2)
			{
				for (size_t i = 1; i < wordids.size(); i ++)
					for (size_t j = 0; j < wordids[0].size(); j ++)
						for (size_t k = 0; k < wordids[i].size(); k ++)
							if (wordids[0][j] != wordids[i][k]
									&& setPairsNotAdded.find(pair<int, int>(
										wordids[0][j], wordids[i][k])) == setPairsNotAdded.end()
									&& setPairsNotAdded.find(pair<int, int>(
										wordids[i][k], wordids[0][j])) == setPairsNotAdded.end())
								synonyms[wordids.back()[k]].insert(wordids[i][j]);
			}
			else if (type == 3)
			{
				for (size_t i = 1; i < wordids.size(); i ++)
					for (size_t j = 0; j < wordids[0].size(); j ++)
						for (size_t k = 0; k < wordids[i].size(); k ++)
							if (wordids[0][j] != wordids[i][k]
									&& setPairsNotAdded.find(pair<int, int>(
										wordids[0][j], wordids[i][k])) == setPairsNotAdded.end()
									&& setPairsNotAdded.find(pair<int, int>(
										wordids[i][k], wordids[0][j])) == setPairsNotAdded.end())
								synonyms[wordids[0][j]].insert(wordids[i][k]);
			}
		}
		file.close();
	}

	if (a < argc)
	{
		cout << "Build synonyms dictionary." << endl;
		cout << "Usage: " << argv[0] << " <-s|-b|-p> WordIdFile ... -o OutputFile" << endl;
		cout << "  -s  synonyms list. words are synonyms to each others." << endl;
		cout << "  -b  brand information. first word is brand, other words are products." << endl;
		cout << "  -p  product information. first word is product, other words are product types." << endl;
		cout << "  -o  output file." << endl;
		return -1;
	}

	XTrie dict;
	for (map<int, set<int> >::const_iterator it = synonyms.begin();
			it != synonyms.end(); ++ it)
	{
		vector<int> syns(it->second.begin(), it->second.end());
		dict.Add((void *)(&it->first), sizeof(int),
				(void *)(&syns[0]), sizeof(int) * syns.size());
		cout << it->first << "\t" << syns[0];
		for (size_t i = 1; i < syns.size(); i ++)
			cout << " " << syns[i];
		cout << endl;
	}
	dict.Save(dictname.c_str());

	return 0;
}
