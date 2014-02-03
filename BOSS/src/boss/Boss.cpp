#include "Boss.h"
#include "StringSplit.h"
#include "strnormalize.h"
#include "GlobalFunction.h"
#include <stdarg.h>
#include <iostream>
#include <sstream>
#include <fstream>
#include "XConfig.h"

using namespace std;
using namespace b2bmlr;

CBoss::CBoss()
{
}

CBoss::~CBoss()
{
}

bool CBoss::loadWordDict(const string& strFile)
{
    Container<Trie<Vector<char> > > word_dict(strFile.c_str());
    m_ftWordDict = word_dict;
    return true;
}

bool CBoss::loadWordTagDict(const string& strFile)
{
    Container<Trie<Vector<char> > > word_tag_dict(strFile.c_str());
    m_ftWordTagDict = word_tag_dict;
    return true;
}

bool CBoss::init(const char* pConf)
{
    XConfig conf;
    if (!conf.Load(pConf))  {
        cerr << "Error: cannot open boss configure file:" << pConf << endl;
        return false;
    }
	string strFile;
    if (!conf.GetItem("Files", "WORD_DICT", strFile)){
		cerr << "Error: cannot find WORD_DICT in " << pConf << endl;
		return false;
	}

    if (!loadWordDict(strFile)){
		cerr << "Error: cannot load word dict in " << pConf << endl;
		return false;
	}

    if (!conf.GetItem("Files", "WORD_TAG_DICT", strFile)){
		cerr << "Error: cannot find WORD_TAG_DICT in " << pConf << endl;
		return false;
	}

    if (!loadWordTagDict(strFile)){
		cerr << "Error: cannot load word tag dict in " << pConf << endl;
		return false;
	}

    return true;
}

int CBoss::find_word_dict(const string& word)
{
    Vector<char> address;
    if (m_ftWordDict[0].match((uint8_t *)(word.c_str())
            , (uint8_t *)(word.c_str()+word.size()), &address))
        return atoi(string(address.begin(), address.end()).c_str());

    return 0;
}

map<string, int> CBoss::find_word_tag_dict(const string& word)
{
    map<string, int> mapTagCount;
    Vector<char> address;
    if (m_ftWordTagDict[0].match((uint8_t *)(word.c_str())
            , (uint8_t *)(word.c_str()+word.size()), &address))
    {
        string strInfo(address.begin(), address.end());
        StringSplit kv_spliter(strInfo.c_str(), "\1", true);

        for (int i = 0; i < kv_spliter.Size(); i ++){
            StringSplit sub_spliter(kv_spliter[i], "\2", true);
            if (sub_spliter.Size() != 2)
                continue;
            mapTagCount[sub_spliter[0]] = atoi(sub_spliter[1]);
        }
    }
    return mapTagCount;

}

void CBoss::get_sub_terms(const string& strTerm, vector<string>& vecSubTerms)
{
    vecSubTerms.clear();
    vector<string> vecChars;
    splitUtf8String(strTerm.c_str(), vecChars);

    if (vecChars.size() <= 2)   {
        vecSubTerms.push_back(strTerm);
        return;
    }
    int nTermPro = find_word_dict(strTerm);
    
    string strTermA;
    for (int i = 0;i < vecChars.size()-1; i ++)
        strTermA += vecChars[i];
    int nTermAPro = find_word_dict(strTermA);

    if ((double)nTermAPro/nTermPro >= 0.3){
        vecSubTerms.push_back(strTermA);
        vecSubTerms.push_back(vecChars[vecChars.size()-1]);
        return;
    }

    string strTermB;
    for (int i = 1;i < vecChars.size(); i ++)
        strTermB += vecChars[i];
    int nTermBPro = find_word_dict(strTermB);

    if ((double)nTermBPro/nTermPro >= 0.3){
        vecSubTerms.push_back(vecChars[0]);
        vecSubTerms.push_back(strTermB);
        return;
    }

    vecSubTerms.push_back(strTerm);
    return;
}

void CBoss::process_ws(const string &strOrigin, const int& nProcessType, vector<string>& vecTerms)
{
    if (strOrigin.size() == 0)
        return;

    vector<string> vecChars;
    splitUtf8String(strOrigin.c_str(), vecChars );

    vector<string> vecMaxTokens;
    for(int i = vecChars.size()-1; i >= 0; ){
        string word_adjoint = vecChars[i];
        int max = i;
        string max_word_adjoint = word_adjoint;
        for (int j = i-1; j >= 0 && j >= i-6; j --){
            word_adjoint = vecChars[j]+word_adjoint;
            if(find_word_dict(word_adjoint) != 0)
            {
                max = j;
                max_word_adjoint = word_adjoint;
            }
        }
        i = max-1;
        vecMaxTokens.insert(vecMaxTokens.begin(), max_word_adjoint);
    }

    for (int i = 0; i < (int)vecMaxTokens.size()-1; i ++){
        if (vecMaxTokens[i].size() != 3 || vecMaxTokens[i+1].size() <= 5)
            continue;
        vector<string> vecChars;
        splitUtf8String((vecMaxTokens[i]+vecMaxTokens[i+1]).c_str(), vecChars);
        string a = vecChars[0]+vecChars[1];
        string b = "";
        for (int j = 2; j < (int)vecChars.size(); j ++)
            b += vecChars[j];
        if (find_word_dict(a) != 0 && find_word_dict(b) != 0) 
        {
            double prob_ab = find_word_dict(a)/1000000.0*find_word_dict(b);
            double prob_ori = find_word_dict(vecMaxTokens[i])/1000000.0*find_word_dict(vecMaxTokens[i+1]);

            if (prob_ab > prob_ori){
                vecMaxTokens[i] = a;
                vecMaxTokens[i+1] = b;
            }
        }
    }
    vector<vector<string> > vecSplitTmp;
    vector<int> vecSplitIndex;
    for (int i = 0; i < (int)vecMaxTokens.size()-1; i ++){
        if (vecMaxTokens[i].size() < 12)
            continue;
        vector<string> vecChars;
        splitUtf8String(vecMaxTokens[i].c_str(), vecChars );
        double prob_ori = find_word_dict(vecMaxTokens[i])/1000000.0*4752474;
        for (int k = 1; k < (int)vecChars.size(); k ++){
            string a, b;
            for (int m = 0; m < k ; m ++)
                a += vecChars[m];
            if (find_word_dict(a) == 0)
                continue;
            for (int n = k; n < (int)vecChars.size() ; n ++)
                b += vecChars[n];
            if (find_word_dict(b) == 0)
                continue;
            double prob_ab = find_word_dict(a)/1000000.0*find_word_dict(b);
            if (prob_ab*15 > prob_ori){
                vector<string> tmp;
                tmp.push_back(a);
                tmp.push_back(b);
                vecSplitTmp.push_back(tmp);
                vecSplitIndex.push_back(i);
                break;
            }
        }
    }

    for (int i = vecSplitIndex.size()-1; i >= 0; i--)
    {
        int index = vecSplitIndex[i];
        vecMaxTokens.erase(vecMaxTokens.begin()+index, vecMaxTokens.begin()+index+1);
        vecMaxTokens.insert(vecMaxTokens.begin()+index, vecSplitTmp[i].begin(), vecSplitTmp[i].end());
    }

    for(int i = 0;i < (int)vecMaxTokens.size(); i ++){
        if (nProcessType == 1){
            vector<string> vecSubTerms;
            get_sub_terms(vecMaxTokens[i], vecSubTerms);
            vecTerms.insert(vecTerms.end(), vecSubTerms.begin(), vecSubTerms.end());
        }
        else{
            vecTerms.push_back(vecMaxTokens[i]);
        }
    }
}

void CBoss::process_tw(const string &strOrigin, const int& nProcessType, vector<CToken>& vecTokens)
{
    if (strOrigin.size() == 0 || vecTokens.size() == 0)
        return;

    for (int i = 0; i < (int)vecTokens.size(); i ++){
        map<string, int> mapTagCount = find_word_tag_dict(vecTokens[i].strTerm);
        if (mapTagCount.size() == 0){
            vecTokens[i].strTag = "OTHER";
            continue;
        }
        string strTag = "";
        int nMaxWeight = 0;
        for (map<string, int>::iterator sub_it = mapTagCount.begin(); sub_it != mapTagCount.end(); sub_it ++)
        {
            if (sub_it->second >= nMaxWeight){
                strTag = sub_it->first;  
                nMaxWeight = sub_it->second;
            }
        }

        vecTokens[i].strTag = strTag;
    }

    int start = 0;
    while ( start < (int)vecTokens.size()){
        int end = 0;
        for (end = start+1; end < (int)vecTokens.size(); end ++){
            if (vecTokens[end].strTag == "BL")
                break;
        }
        int nCpCoreCount = 0;
        for (int i = start; i < end; i ++){
            if (vecTokens[i].strTag == "CP_CORE")
                nCpCoreCount += 1;
        }
        if (nCpCoreCount == 0){
            for (int i = end-1;i >= start; i-- ){
                if (vecTokens[i].strTag == "CP_XIUSHI"){
                    vecTokens[i].strTag = "CP_CORE";
                    break;
                }
            }
        }
        start = end;
    }

    int nWeightSum = 0;
    for (int i = 0; i < (int)vecTokens.size(); i ++){
        if (vecTokens[i].strTag.find("CP") == 0){
            vecTokens[i].nWeight = 40;
        }
        else if (vecTokens[i].strTag.find("PP") == 0){
            vecTokens[i].nWeight = 30;
        }
        else if (vecTokens[i].strTag.find("XH") == 0){
            vecTokens[i].nWeight = 20;
        }
        else if (vecTokens[i].strTag.find("BL") == string::npos 
                && vecTokens[i].strTag.find("LKH") == string::npos 
                && vecTokens[i].strTag.find("RKH") == string::npos 
                && vecTokens[i].strTag.find("PZH") == string::npos 
                ){ 
            vecTokens[i].nWeight = 10;
        }
        else{
            vecTokens[i].nWeight = 0;
        }
        if (vecTokens[i].strTag.find("CORE") != string::npos){
            vecTokens[i].nWeight += 10;
        }
        nWeightSum += vecTokens[i].nWeight;
    }

    for (int i = 0; i < (int)vecTokens.size(); i ++){
        vecTokens[i].nWeight = (int)((double)(vecTokens[i].nWeight)/nWeightSum*100);
        vecTokens[i].transTag2Type();
    }


}

vector<CToken> CBoss::process(const string &strOrigin, const int& nProcessType)
{
    vector<CToken> vecTokens;
    vector<string> vecTerms;
    process_ws(strOrigin, nProcessType, vecTerms); 
    for (int i = 0; i < (int)vecTerms.size(); i ++)
    {
        CToken token;
        token.strTerm = vecTerms[i];
        vecTokens.push_back(token);
    }
    process_tw(strOrigin, nProcessType, vecTokens);
	return vecTokens; 
}


bool CBoss::process(const string &strOrigin, const int& nProcessType, vector<CToken>& vecTokens)
{
    vecTokens.clear();
    vector<string> vecTerms;
    process_ws(strOrigin, nProcessType, vecTerms); 
    for (int i = 0; i < (int)vecTerms.size(); i ++)
    {
        CToken token;
        token.strTerm = vecTerms[i];
        vecTokens.push_back(token);
    }
    process_tw(strOrigin, nProcessType, vecTokens);
    //cout << "strOrigin:" << strOrigin << "\tvecTokens size:" << vecTokens.size() << endl;

    return true;
}


