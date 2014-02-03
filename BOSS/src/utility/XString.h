///////////////////////////////////////////
//  * File:    XString.h
//  * Author:  XingFei
//  * Contact: fei.xing@alibaba-inc.com
///////////////////////////////////////////


#ifndef XSTRING_H_
#define XSTRING_H_

#include <string>
#include <string.h>
#include <vector>
#include "stdlib.h"
#include "string.h"
#include "stdint.h"

namespace b2bmlr {

	class XString : public std::string
	{
	public:
		XString();
		XString(const char* ptr, std::string::size_type count);
		XString(const char* ptr);
		XString(const std::string& str, std::string::size_type roff = 0, std::string::size_type count = std::string::npos);
		XString(const std::string::size_type count, std::string::value_type ch);
		virtual ~XString();
		void Clear();

		//Replace in the string the first occurrence of the specified substring with another specified string
		XString& ReplaceOne(const char *csFrom, const char *csTo);
		XString& ReplaceOne(const std::string &strFrom, const std::string &strTo) {
			return ReplaceOne(strFrom.c_str(), strTo.c_str());
		}
		//Replace in the string all occurrences of the specified substring with another specified string
		XString& ReplaceAll(const char *csFrom, const char *csTo);
		XString& ReplaceAll(const std::string &strFrom, const std::string &strTo) {
			return ReplaceAll(strFrom.c_str(), strTo.c_str());
		}

		XString &TrimLeft(const std::string &strTarget=" \r\n\t");
		XString &TrimRight(const std::string &strTarget=" \r\n\t");
		XString &Trim(const std::string &strTarget=" \r\n\t");

		XString &MakeLower();
		XString &MakeUpper();
		XString &MakeLowerGBK();
		XString &MakeUpperGBK();
		XString &NormalizeGBK();

		/**
		* Break the string into tokens
		*/
		void Split(std::vector<XString>& tokens, const std::string& delims) const;

	//Static members
	public:
		static void ReplaceOne(std::string &str, const char *csFrom, const char *csTo);

		static int StrCmp(const char *str1, const char *str2){ return strcmp(str1, str2); }
		static int StrCaseCmp(const char *str1, const char *str2)
		{ 
			return strcasecmp(str1, str2); 
		}

		static void Split(const std::string &str, std::vector<std::string>& tokens, const std::string& delims);
		
		static void MakeLower(std::string &str);
		static void MakeUpper(std::string &str);
		static void MakeLowerGBK(std::string &str);
		static void MakeUpperGBK(std::string &str);
		static void NormalizeGBK(std::string &str);
		static bool IsGBK(unsigned short code);
	}; //class XString

}	//namespace b2bmlr

#endif
