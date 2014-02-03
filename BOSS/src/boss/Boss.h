#ifndef BOSS_H
#define BOSS_H

#include <iostream>
#include <vector>
#include <set>
#include <string>
#include <map>
#include <FastTrie.h>
#include <MMap.h>

using namespace std;
using namespace ft2;

enum TermType
{
        //类型说明
        CP = 0x1 ,            //产品词
        PP = 0x1<<1,          //品牌词
        JG = 0x1<<2,          //机构词
        XH = 0x1<<3,          //产品型号
        XS = 0x1<<4,          //产品修饰词
        QH = 0x1<<5,          //区划词
        ZM = 0x1<<6,          //专有名词
        PT = 0x1<<7,          //普通词
        FW = 0x1<<8,          //服务词
        YS = 0x1<<9,          //运输服务词
        BL = 0x1<<10,          //并列标点符号
        LKH = 0x1<<11,          //左括号
        RKH = 0x1<<12,          //右括号
        PZH = 0x1<<13,          //破折号
        DAILI = 0x1<<14,          //代理

        //以下是功能说明
        OMIT = 0x1<<15,         //是否省略功能
        CORE = 0x1<<16,        //是否中心词功能
        XIUSHI = 0x1<<17,      //产品词是否修饰成分功能
        SEARCH = 0X1<<18      //是否允许被检索到
};

struct CToken
{
public:
    string strTerm;
    string strTag;
    size_t nType;
    int nWeight;
    int nWeightNorm;
public:
	CToken(){strTerm="";strTag="";nType=0;nWeight=0;}
    void transTag2Type(){
        if (strTag.find("CP") == 0)    nType |= CP;
        else if (strTag.find("PP") == 0)    nType |= PP;
        else if (strTag.find("JG") == 0)    nType |= JG;
        else if (strTag.find("XH") == 0)    nType |= XH;
        else if (strTag.find("XS") == 0)    nType |= XS;
        else if (strTag.find("QH") == 0)    nType |= QH;
        else if (strTag.find("ZM") == 0)    nType |= ZM;
        else if (strTag.find("PT") == 0)    nType |= PT;
        else if (strTag.find("FW") == 0)    nType |= FW;
        else if (strTag.find("YS") == 0)    nType |= YS;
        else if (strTag.find("BL") == 0)    nType |= BL;
        else if (strTag.find("LKH") == 0)    nType |= LKH;
        else if (strTag.find("RKH") == 0)    nType |= RKH;
        else if (strTag.find("PZH") == 0)    nType |= PZH;
        else if (strTag.find("DAILI") == 0)    nType |= DAILI;
        
        if (strTag.find("OMIT") != string::npos)    nType |= OMIT;
        else if (strTag.find("CORE") != string::npos)    nType |= CORE;
        else if (strTag.find("XIUSHI") != string::npos)    nType |= XIUSHI; 
        else if (strTag.find("SEARCH") != string::npos)    nType |= SEARCH;
    }
};

class CBoss
{
public:
	CBoss();
	~CBoss();
	bool init(const char*);
	vector<CToken> process(const string &strOrigin, const int& nProcessType);
    bool process(const string &strOrigin, const int& nProcessType, vector<CToken>& vecTokens);

    void process_ws(const string &strOrigin, const int& nProcessType, vector<string>& vecTerms);
    void process_tw(const string &strOrigin, const int& nProcessType, vector<CToken>& vecTokens);
    void get_sub_terms(const string& strTerm, vector<string>& vecSubTerms);

private:
    bool loadWordDict(const string& strFile);
    bool loadWordTagDict(const string& strFile);

    int find_word_dict(const string& word);
    map<string, int> find_word_tag_dict(const string& word);

private:
    map<string, int> m_mapWordDict;
    map<string, map<string, int> > m_mapWordTagDict;
    Container<Trie<Vector<char> > > m_ftWordDict;
    Container<Trie<Vector<char> > > m_ftWordTagDict;
};


#endif
