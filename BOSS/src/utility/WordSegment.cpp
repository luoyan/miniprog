#include "AliWSInterface.h"
#include "WordSegment.h"

using namespace std;
using namespace ws;
using namespace b2bmlr;

WordSegment::~WordSegment()
{
	delete m_ws;
}

bool WordSegment::init(const char *config)
{
	m_ws = new AliWS();
	if (!m_ws->init(config)) return false;
	if (!m_ws->selectSegmenter(ws::B2B)) return false;
	return true;
}

const std::vector<WordSegment::WordInfo> WordSegment::segment(const char *text) const
{
	vector<WORD_UTF8_T> words;
	m_ws->segment(text, strlen(text), words);

	vector<WordInfo> result;
	result.reserve(words.size());
	for (size_t i = 0; i != words.size(); i ++)
		result.push_back(WordInfo(
//				string(words[i].pBuf, words[i].length), words[i].wordId, words[i].tagId));
				string(words[i].pBuf, words[i].length), words[i].wordId, words[i].wordTypes.size()));

	return result;
}

const std::vector<std::string> WordSegment::segmentIntoStrings(const char *text) const
{
	vector<WORD_UTF8_T> words;
	m_ws->segment(text, strlen(text), words);

	vector<string> result;
	result.reserve(words.size());
	for (size_t i = 0; i != words.size(); i ++)
		result.push_back(string(words[i].pBuf, words[i].length));

	return result;
}

const std::vector<int32_t> WordSegment::segmentIntoIds(const char *text) const
{
	vector<WORD_UTF8_T> words;
	m_ws->segment(text, strlen(text), words);

	vector<int32_t> result;
	result.reserve(words.size());
	for (size_t i = 0; i != words.size(); i ++)
		result.push_back(words[i].wordId);

	return result;
}

void WordSegment::segment(const char *text, std::vector<WordSegment::WordInfo> &result) const
{
	vector<WORD_UTF8_T> words;
	m_ws->segment(text, strlen(text), words);

	result.resize(0);
	for (size_t i = 0; i != words.size(); i ++)
		result.push_back(WordInfo(
				string(words[i].pBuf, words[i].length), words[i].wordId, words[i].tagId));
}

void WordSegment::segment(const char *text, std::vector<std::string> &result) const
{
	vector<WORD_UTF8_T> words;
	m_ws->segment(text, strlen(text), words);

	result.resize(0);
	for (size_t i = 0; i != words.size(); i ++)
		result.push_back(string(words[i].pBuf, words[i].length));
}

void WordSegment::segment(const char *text, std::vector<int32_t> &result) const
{
	vector<WORD_UTF8_T> words;
	m_ws->segment(text, strlen(text), words);

	result.resize(0);
	for (size_t i = 0; i != words.size(); i ++)
		result.push_back(words[i].wordId);
}

#ifdef UNIT_TEST_MAIN
#include <iostream>

int main(int argc, char **argv)
{
	if (argc != 2)
	{
		cout << "Usage: ./WordSegment ws.conf < input-utf8.txt > output.txt" << endl;
		return -1;
	}

	WordSegment ws;
	if (!ws.init(argv[1]))
	{
		cout << "error initializing ws" << endl;
		return -1;
	}

	string line;
	while (getline(cin, line))
	{
		vector<WordSegment::WordInfo> result = ws.segment(line.c_str());
		for (size_t i = 0; i != result.size(); i ++)
			cout << result[i].text << "/" << result[i].tagId << " ";
		cout << endl;
	}

	return 0;
}
#endif /* UNIT_TEST_MAIN */
