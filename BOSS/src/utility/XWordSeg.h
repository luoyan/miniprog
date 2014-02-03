///////////////////////////////////////////
//  * File:    XWordSeg.h
//  * Author:  XingFei
//  * Contact: fei.xing@alibaba-inc.com
///////////////////////////////////////////


#ifndef XWORDSEG_H_
#define XWORDSEG_H_

#include <string>
#include <vector>
#include <map>
#include <set>
#include <iconv.h>

#include "StringWS.h"
#include "wsInterface.h"
#include "keeInterface.h"

using namespace ws;
using namespace kee; 
namespace b2bmlr {

class XWordSeg
{
public:
	XWordSeg();
	~XWordSeg();

	bool Init(const char *ywsConfFilePath, ws::WsSegmenter *ws = NULL);
	bool Segment(const char *pUTF8Text, int nTextLen, std::vector<std::string> &vecWords, std::vector<int> &vecTags, WsSegmenter *ws = NULL);
        bool SegmentRM(const char *pUTF8Text, int nTextLen, std::vector<int> &vecWordIDs, std::vector<int> &vecWordTags);
        bool SegmentRM(const char *pUTF8Text, int nTextLen, std::vector<int> &vecWordIDs, std::vector<int> &vecWordTags, WsSegmenter *ws);
	bool Segment(const char *pUTF8Text, int nTextLen, std::vector<int> &vecWordIDs, std::vector<int> &vecTags);
	bool Segment(const std::string &strUTF8Text, std::vector<std::string> &vecWords, std::vector<int> &vecTags, WsSegmenter *ws = NULL);
	bool Segment(const std::string &strUTF8Text, std::vector<int> &vecWordIDs, std::vector<int> &vecTags);
        bool SegmentRM(const std::string &strUTF8Text, std::vector<int> &vecWordIDs, std::vector<int> &vecWordTags);
        bool SegmentRM(const std::string &strUTF8Text, std::vector<int> &vecWordIDs, std::vector<int> &vecWordTags, WsSegmenter *ws);
        bool Segment(StringWS &strws);
        bool Segment(StringWS &strws, WsSegmenter *ws);
	const std::string Utf8ToGbk(const std::string &strUtf8Text);
	const std::string GbkToUtf8(const std::string &strGBKText);
	bool SetRemoveWordList(const std::vector<std::string> &vecWords);
        int ChooseSX(std::vector<WordType> &wordtp, int nClass);
        int FindIndex(std::vector<vector<WORD_T> > &vec2Words, int nKeyWordIndex);
        bool XWordSeg::LoadBwsWordtags(const string &config, ws::WsSegmenter *ws);

private:
	ws::WsSegmenter *m_pYWS;
        kee::KeyEntityExtractor *m_pKEE;

	iconv_t m_cdGbkToUtf8;
	iconv_t m_cdUtf8ToGbk;

	std::set<std::string> m_setRemoveWords;
	std::set<int> m_setRemoveWordIDs;
        std::map<int, int> m_mapBwsWordtags;
};

}
	
#endif
