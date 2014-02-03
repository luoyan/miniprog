/*
 * =====================================================================================
 *
 *       Filename:  GlobalFunction.h
 *
 *    Description:  
 *
 *        Version:  1.0
 *        Created:  2012年06月14日 17时22分59秒
 *       Revision:  none
 *       Compiler:  gcc
 *
 *         Author:  YOUR NAME (), 
 *        Company:  
 *
 * =====================================================================================
 */
#ifndef GLOBAL_FUCTION_H 
#define GLOBAL_FUCTION_H 
#include <string>
#include <vector>
#include <set>
#include "Logger.h" 
#include "StringSplit.h" 
#include "strnormalize.h" 

using namespace std;
using namespace comm_tools;

void splitUtf8String(const char *pString, vector<string>& vecChars );

#endif
