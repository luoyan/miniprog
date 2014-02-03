/*
 * =====================================================================================
 *
 *       Filename:  GlobalFunction.cpp
 *
 *    Description:  :wq
 *
 *
 *        Version:  1.0
 *        Created:  2012年06月14日 17时23分26秒
 *       Revision:  none
 *       Compiler:  gcc
 *
 *         Author:  YOUR NAME (), 
 *        Company:  
 *
 * =====================================================================================
 */
#include "GlobalFunction.h"
#include <string>
#include <vector>
#include <set>
#include <string.h>
#include <fstream>
#include "utf8.h"

using namespace std;
using namespace b2bmlr;

void splitUtf8StringOld(const char *pString, vector<string>& vecChars )
{
	size_t i = 0, length = 0;
	while (i < strlen(pString))	{
		if (pString[i] & 0x80){
			length = 3;
		}
		else if (pString[i] == ' ' || pString[i] == ';' || pString[i] == '/' || pString[i] == '\\' || pString[i] == '·'){
			length = 1;
		}
		else	{
			length = 1;
			while (i+length < strlen(pString) && !(pString[i+length] & 0x80) 
				&& pString[i+length] != ' ' &&  pString[i+length] != ';'
				&& pString[i+length] != '/' &&  pString[i+length] != '\\'
                )
				length ++;
		}
		vecChars.push_back(string(pString+i,length));
		i += length;
	}
	return;
}

void splitUtf8String(const char *pString, vector<string>& vecChars )
{
    const char* str = pString;    // utf-8 string
    const char* end = pString+strlen(pString)+1;      // end iterator
    vector<string> vecCharsTmp;
    do
    {
        char symbol[5] = {0,0,0,0,0};
        uint32_t code = utf8::next(str, end); // get 32 bit code of a utf-8 symbol
        if (code == 0)
            continue;

        utf8::append(code, symbol); // initialize array `symbol`
        vecCharsTmp.push_back(string((char *)symbol));
    }
    while ( str < end );
   
    for (int i = 0; i< vecCharsTmp.size(); i ++) 
    {
        if(vecCharsTmp[i].size() == 1){
            int k = i;
            string tmp;
            while (k < vecCharsTmp.size()){
               if (vecCharsTmp[k].size() == 1 && ((vecCharsTmp[k][0] >= 'a' && vecCharsTmp[k][0] <= 'z') || (vecCharsTmp[k][0] >= 'A' && vecCharsTmp[k][0] <= 'Z') || (vecCharsTmp[k][0] >= '0' && vecCharsTmp[k][0] <= '9') || vecCharsTmp[k][0] == '-' || vecCharsTmp[k][0] == '_' ) || vecCharsTmp[k][0] == '.'  || vecCharsTmp[k][0] == '#' || vecCharsTmp[k][0] == '%'  ) {
                   tmp += vecCharsTmp[k];
                   k ++;
               }
               else{
                   break;
               }
            }
            i = k;
            if (tmp.size() >= 1)
                vecChars.push_back(tmp);
            if (k <= vecCharsTmp.size()-1)
                vecChars.push_back(vecCharsTmp[k]);
        }
        else	vecChars.push_back(vecCharsTmp[i]);
    }
    
    return;
}



int main()
{
    //string strword = "世界中国test你好hello什么事- nishuo测试a哈哈";
    string strword = "";
    vector<string> a;
    splitUtf8String(strword.c_str(), a);
    for (int i = 0; i < a.size(); i ++){
        cout << a[i] << "|";
    }
    cout << endl;
}

