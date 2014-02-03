#include <iostream>
#include <fstream>
#include <map>
#include <ext/hash_map>

using namespace std;
using namespace __gnu_cxx;

void output(string query, map<unsigned, unsigned> &counter)
{
	unsigned sum = 0;
	unsigned max = 0;
	for (map<unsigned, unsigned>::iterator it = counter.begin(); it != counter.end(); )
	{
		sum += it->second;
		if (it->second > max) max = it->second;
		if (it->second < 3) counter.erase(it ++);
		else ++ it;
	}

	if (counter.empty() || max < 30 || max < sum * 0.20) return;

	cout << query << "\t";
	for (map<unsigned, unsigned>::const_iterator it = counter.begin(); it != counter.end(); ++ it)
	{
		if (it != counter.begin()) cout << " ";
		cout << it->first << " " << it->second / (double)max;
	}
	cout << endl;
}

int main(int argc, char **argv)
{
	if (argc != 3)
	{
		cout << "usage: ./offercat offer-cat query-offer-num.seg.sorted > clickcount.txt" << endl;
		return 0;
	}

	string line;

	hash_map<unsigned, unsigned> mapOfferCatIds;

	ifstream ifQueryOfferCount(argv[2]);
	while (getline(ifQueryOfferCount, line))
	{
		size_t t1 = line.find('\t');
		size_t t2 = line.find('\t', t1 + 1);
		if (t1 <= 1 || t1 == string::npos || t2 == string::npos) continue;
		string strQuery = line.substr(0, t1 - 1);
		unsigned uOfferId = atoi(line.substr(t1 + 1, t2 - t1 - 1).c_str());

		mapOfferCatIds[uOfferId] = (unsigned)(-1);
	}
	ifQueryOfferCount.close();
	ifQueryOfferCount.clear();

	ifstream ifOfferCat(argv[1]);
	while (getline(ifOfferCat, line))
	{
		size_t t = line.find('\t');
		if (t == string::npos) continue;
		unsigned uOfferId = atoi(line.substr(0, t).c_str());
		unsigned uCatId = atoi(line.substr(t + 1).c_str());

		hash_map<unsigned, unsigned>::iterator it = mapOfferCatIds.find(uOfferId);
		if (it != mapOfferCatIds.end())
			it->second = uCatId;
	}
	ifOfferCat.close();

	string strLastQuery;
	map<unsigned, unsigned> mapCatCounts;
	
	ifQueryOfferCount.open(argv[2]);
	while (getline(ifQueryOfferCount, line))
	{
		size_t t1 = line.find('\t');
		size_t t2 = line.find('\t', t1 + 1);
		if (t1 <= 1 || t1 == string::npos || t2 == string::npos) continue;
		string strQuery = line.substr(0, t1 - 1);
		unsigned uOfferId = atoi(line.substr(t1 + 1, t2 - t1 - 1).c_str());
		unsigned uCount = atoi(line.substr(t2 + 1).c_str());

		if (strQuery != strLastQuery)
		{
			if (!strLastQuery.empty() && !mapCatCounts.empty())
				output(strLastQuery, mapCatCounts);

			strLastQuery = strQuery;
			mapCatCounts.clear();
		}

		hash_map<unsigned, unsigned>::iterator it = mapOfferCatIds.find(uOfferId);
		if (it != mapOfferCatIds.end() && it->second != (unsigned)(-1))
			mapCatCounts[it->second] += uCount;
	}
	ifQueryOfferCount.close();

	if (!strLastQuery.empty() && !mapCatCounts.empty())
		output(strLastQuery, mapCatCounts);

	return 0;
}
