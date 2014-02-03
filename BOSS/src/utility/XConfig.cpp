///////////////////////////////////////////
//  * File:    XConfig.h
//  * Author:  XingFei
//  * Contact: fei.xing@alibaba-inc.com
///////////////////////////////////////////


#include "XConfig.h"
#include <iostream>
#include <fstream>
#include <stdio.h>
#include "XString.h"

using namespace std;

namespace b2bmlr {

///////////////////////////////////////////////////////////////////////////////
// Public methods
///////////////////////////////////////////////////////////////////////////////

XConfig::XConfig()
{
}

XConfig::~XConfig()
{
}

void XConfig::Clear()
{
	m_strLastError = "";
	m_groups.clear();
}

bool XConfig::Load(const string &strFile)
{
	bool bRet = true;
	ifstream fi(strFile.c_str());
	if(!fi) { return false; }

	XString strLine;
	CfgGroup curGroup;
	CfgItem curItem;
	string strCurComments;

	while(bRet && getline(fi, strLine)) {
		strLine.Trim();
		if(strLine.size() <= 0 || strLine[0] == '#' || strLine[0] == ';') {
			if(strCurComments.size() > 0) {
				strCurComments += "\n";
			}
			strCurComments += strLine;
			continue;
		}

		if(IsGroupNameLine(strLine)) {
			if(curGroup.m_items.size() > 0) {
				m_groups.insert(curGroup);
			}

			bRet = ParseGroupLine(strLine, curGroup);
			curGroup.m_items.clear();
			curGroup.m_strComments = strCurComments;
		}
		else {
			bRet = ParseItemLine(strLine, curItem);
			curItem.m_strComments = strCurComments;
			curGroup.m_items.insert(curItem);
		}

		if(!bRet) {
			m_strLastError = "Invalid line: " + strLine;
		}
	}

	if(curGroup.m_items.size() > 0) {
		m_groups.insert(curGroup);
	}

	fi.close();
	return bRet;
}

bool XConfig::Store(const std::string &strFile)
{
	ofstream fo(strFile.c_str());
	if(!fo) {
		return false;
	}

	GroupSet::iterator iterGroup = m_groups.begin();
	for(; iterGroup != m_groups.end(); iterGroup++) {
		const CfgGroup &group = *iterGroup;

		fo << group.m_strComments << endl;
		fo << "[" << group.m_strName << "]" << endl;

		ItemSet::iterator iterItem = group.m_items.begin();
		for(; iterItem != group.m_items.end(); iterItem++) {
			const CfgItem &item = *iterItem;

			fo << item.m_strComments << endl;
			fo << item.m_strKey << " = " << item.m_strValue << endl;
		}
	}

	bool bRet = fo.good();
	fo.close();
	return bRet;
}

bool XConfig::GetItem(const std::string &strGroup, const std::string &strItem, bool &bVal)
{
	CfgItem *pItem = GetItem(strGroup, strItem);
	if(pItem != NULL) {
		int nVal = atoi(pItem->m_strValue.c_str());
		if(nVal != 1 && nVal != 0)
			return false;
		bVal = (nVal == 1 ? true : false);
		return true;
	}
	else 
		return false;
}

bool XConfig::GetItem(const std::string &strGroup, const std::string &strItem, int &nVal)
{
	CfgItem *pItem = GetItem(strGroup, strItem);
	if(pItem != NULL) {
		nVal = atoi(pItem->m_strValue.c_str());
		return true;
	}
	else 
		return false;
}

bool XConfig::GetItem(const std::string &strGroup, const std::string &strItem, unsigned int &nVal)
{
	CfgItem *pItem = GetItem(strGroup, strItem);
	if(pItem != NULL) {
		nVal = atoi(pItem->m_strValue.c_str());
		return true;
	}
	else 
		return false;
}

bool XConfig::GetItem(const std::string &strGroup, const std::string &strItem, double &fVal)
{
	CfgItem *pItem = GetItem(strGroup, strItem);
	if(pItem != NULL) {
		fVal = atof(pItem->m_strValue.c_str());
		return true;
	}
	else 
		return false;
}

bool XConfig::GetItem(const std::string &strGroup, const std::string &strItem, std::string &strVal)
{
	CfgItem *pItem = GetItem(strGroup, strItem);
	if(pItem != NULL) {
		strVal = pItem->m_strValue;
		return true;
	}
	else 
		return false;
}

bool XConfig::SetItem(const std::string &strGroup, const std::string &strItem, bool &bVal)
{
	CfgItem *pItem = GetItem(strGroup, strItem);
	if(pItem != NULL) {
		char szBuf[20];
		if(sprintf(szBuf, "%d", (bVal?1:0)) == -1)
			return false;
		pItem->m_strValue = szBuf;
		return true;
	}
	else 
		return false;
}

bool XConfig::SetItem(const std::string &strGroup, const std::string &strItem, int &nVal)
{
	CfgItem *pItem = GetItem(strGroup, strItem);
	if(pItem != NULL) {
		char szBuf[20];
		if(sprintf(szBuf, "%d", nVal) == -1)
			return false;
		pItem->m_strValue = szBuf;
		return true;
	}
	else 
		return false;
}

bool XConfig::SetItem(const std::string &strGroup, const std::string &strItem, unsigned int &nVal)
{
	CfgItem *pItem = GetItem(strGroup, strItem);
	if(pItem != NULL) {
		char szBuf[20];
		if(sprintf(szBuf, "%d", nVal) == -1)
			return false;
		pItem->m_strValue = szBuf;
		return true;
	}
	else 
		return false;
}

bool XConfig::SetItem(const std::string &strGroup, const std::string &strItem, double &fVal)
{
	CfgItem *pItem = GetItem(strGroup, strItem);
	if(pItem != NULL) {
		char szBuf[20];
		if(sprintf(szBuf, "%f", fVal) == -1)
			return false;
		pItem->m_strValue = szBuf;
		return true;
	}
	else 
		return false;
}

bool XConfig::SetItem(const std::string &strGroup, const std::string &strItem, std::string &strVal)
{
	CfgItem *pItem = GetItem(strGroup, strItem);
	if(pItem != NULL) {
		pItem->m_strValue = strVal;
		return true;
	}
	else 
		return false;
}

///////////////////////////////////////////////////////////////////////////////
// Non-public methods
///////////////////////////////////////////////////////////////////////////////

bool XConfig::IsGroupNameLine(const std::string &strLine)
{
	if(strLine.size() < 0) { return false; }
	if(strLine[0] == '[' && strLine.find(']') != string::npos) {
		return true;
	}
	return false;
}

bool XConfig::ParseGroupLine(const std::string& strLine, CfgGroup &group)
{
	string::size_type posBegin = strLine.find("[");
	string::size_type posEnd = strLine.find("]");

	if(posBegin != string::npos && posEnd != string::npos && posEnd > posBegin) {
		group.m_strName = strLine.substr(posBegin+1, posEnd-posBegin-1);
	}

	return true;
}

bool XConfig::ParseItemLine(const std::string& strLine, CfgItem &item)
{
	string::size_type pos = strLine.find("=");
	if(pos == string::npos) {
		return false;
	}

	XString strKey = strLine.substr(0, pos);
	XString strValue = strLine.substr(pos+1);

	strKey.Trim();
	item.m_strKey = strKey;
	strValue.Trim();
	item.m_strValue = strValue;

	return strKey.size() > 0;
}

XConfig::CfgGroup* XConfig::GetGroup(const std::string &strGroup)
{
	CfgGroup group;
	group.m_strName = strGroup;
	GroupSet::iterator iterGroup = m_groups.find(group);
	if(iterGroup == m_groups.end()) {
		return NULL;
	}
	CfgGroup *pGroup = (CfgGroup *)&(*iterGroup);

	return pGroup;
}

XConfig::CfgItem* XConfig::GetItem(const std::string &strGroup, const std::string &strItem)
{
	CfgGroup group;
	group.m_strName = strGroup;
	GroupSet::iterator iterGroup = m_groups.find(group);
	if(iterGroup == m_groups.end()) {
		return NULL;
	}
	const CfgGroup *pGroup = &(*iterGroup);

	CfgItem item;
	item.m_strKey = strItem;
	ItemSet::iterator iterItem = pGroup->m_items.find(item);
	if(iterItem == pGroup->m_items.end()) {
		return NULL;
	}
	CfgItem *pItem = (CfgItem *)&(*iterItem);

	return pItem;
}

} //namespace b2bmlr
