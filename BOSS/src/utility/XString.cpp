///////////////////////////////////////////
//  * File:    XString.cpp
//  * Author:  XingFei
//  * Contact: fei.xing@alibaba-inc.com
///////////////////////////////////////////


#include "XString.h"
#include "strnormalize.h"
#include <ctype.h>

using namespace std;

namespace b2bmlr {

XString::XString() : string()
{
}

XString::XString(const char* ptr, string::size_type count)
	: string(ptr, count)
{
}

XString::XString(const char* ptr)
	: string(ptr)
{
}

XString::XString(const string& str, string::size_type roff, string::size_type count)
	: string(str, roff, count)
{
}

XString::XString(const string::size_type count, string::value_type ch)
	: string(count, ch)
{
}

XString::~XString()
{
}

void XString::Clear()
{
	this->erase(this->begin(),this->end());
}

XString &XString::TrimLeft(const string &strTarget)
{
	string::size_type pos = find_first_not_of(strTarget);
	if(pos != string::npos)
	{
		this->assign(this->substr(pos));
	}
	else Clear();

	return (*this);
}

XString &XString::TrimRight(const string &strTarget)
{
	string::size_type pos = find_last_not_of(strTarget);
	if(pos != string::npos)
	{
		this->assign(this->substr(0, pos+1));
	}
	else Clear();

	return (*this);
}

XString &XString::Trim(const string &strTarget)
{
	TrimLeft(strTarget);
	TrimRight(strTarget);
	return (*this);
}

XString &XString::MakeLower()
{
	for(int i = 0; i < (int)length(); i++)
		(*this)[i] = tolower(at(i));
	return(*this);
}

XString &XString::MakeUpper()
{
	for(int i = 0; i < (int)length(); i++)
		(*this)[i] = toupper(at(i));
	return(*this);
}

XString &XString::MakeLowerGBK()
{
	for (int i = 0; i < (int)length(); i ++)
		if ((*this)[i] < 0) i ++;
		else if ((*this)[i] >= 'A' && (*this)[i] <= 'Z')
			(*this)[i] += 'a' - 'A';
	return (*this);
}

XString &XString::MakeUpperGBK()
{
	for (int i = 0; i < (int)length(); i ++)
		if ((*this)[i] < 0) i ++;
		else if ((*this)[i] >= 'a' && (*this)[i] <= 'z')
			(*this)[i] += 'A' - 'a';
	return (*this);
}

XString &XString::NormalizeGBK()
{
	strnormalize_gbk(*this, SNO_TO_LOWER | SNO_TO_HALF | SNO_TO_SIMPLIFIED);
	return (*this);
}

/** Replace in the string the first occurrence of the specified substring with another specified string */
XString& XString::ReplaceOne(const char *csFrom, const char *csTo)
{
	string::size_type idx = find(csFrom);
	if(idx != string::npos)
	{
		replace(idx, strlen(csFrom), csTo);
	}

	return *this;
}

/** Replace in the string all occurrences of the specified substring with another specified string */
XString& XString::ReplaceAll(const char *csFrom, const char *csTo)
{
	string::size_type idx, nBeginPos = 0;
	while(true)
	{
		idx = find(csFrom, nBeginPos);
		if(idx != string::npos) {
			replace(idx, strlen(csFrom), csTo);
			nBeginPos = idx + strlen(csTo);
		}
		else {
			break;
		}
	}

	return *this;
}

/**
 * Break the string into tokens
 */
void XString::Split(vector<XString>& tokens, const string& delims) const
{
	tokens.clear();

	string strItem;
	string::size_type nBeginIdx, nEndIdx;

	nBeginIdx = find_first_not_of(delims);
	while(nBeginIdx != string::npos) //while beginning of a token found
	{
		//search end of the token
		nEndIdx = find_first_of(delims, nBeginIdx);

		//
		if(nEndIdx == string::npos)
			strItem = substr(nBeginIdx, nEndIdx);
		else strItem = substr(nBeginIdx, nEndIdx-nBeginIdx);
		tokens.push_back(strItem);

		//
		nBeginIdx = find_first_not_of(delims, nEndIdx);
	}
}

void XString::ReplaceOne(string &str, const char *csFrom, const char *csTo)
{
	string::size_type idx = str.find_first_of(csFrom);
	if(idx != string::npos)
	{
		str.replace(idx, strlen(csFrom), csTo);
	}
}

void XString::Split(const string &str, std::vector<std::string>& tokens, const std::string& delims)
{
	tokens.clear();

	string strItem;
	string::size_type nBeginIdx, nEndIdx;

	nBeginIdx = str.find_first_not_of(delims);
	while(nBeginIdx != string::npos) //while beginning of a token found
	{
		//search end of the token
		nEndIdx = str.find_first_of(delims, nBeginIdx);

		//
		if(nEndIdx == string::npos)
			strItem = str.substr(nBeginIdx, nEndIdx);
		else strItem = str.substr(nBeginIdx, nEndIdx-nBeginIdx);
		tokens.push_back(strItem);

		//
		nBeginIdx = str.find_first_not_of(delims, nEndIdx);
	}
}

void XString::MakeLower(std::string &str)
{
	for(int i = 0; i < (int)str.length(); i++)
		str[i] = tolower(str[i]);
}

void XString::MakeUpper(std::string &str)
{
	for(int i = 0; i < (int)str.length(); i++)
		str[i] = toupper(str[i]);
}

void XString::MakeLowerGBK(std::string &str)
{
	for (int i = 0; i < (int)str.length(); i ++)
		if (str[i] < 0) i ++;
		else if (str[i] >= 'A' && str[i] <= 'Z')
			str[i] += 'a' - 'A';
}

void XString::MakeUpperGBK(std::string &str)
{
	for (int i = 0; i < (int)str.length(); i ++)
		if (str[i] < 0) i ++;
		else if (str[i] >= 'a' && str[i] <= 'z')
			str[i] += 'A' - 'a';
}

void XString::NormalizeGBK(std::string &str)
{
	strnormalize_gbk(str);
}

bool XString::IsGBK(unsigned short code)
{
	return is_gbk(code);
}


}	//namespace b2bmlr
