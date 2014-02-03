#include <iostream>

#include "XTrie.h"

using namespace std;
using namespace b2bmlr;

int main(int argc, char **argv)
{
	XTrie dict;

	string line;
	while (getline(cin, line))
	{
//		size_t t = line.find('\t');
//		if (t == string::npos) continue;

		int key, value;
		key = atoi(line.c_str());
		value = 0;

		dict.Add((void *)&key, sizeof(int), (void *)&value, sizeof(int));
	}
	dict.Save(argv[1]);

	return 0;
}
