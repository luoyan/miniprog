///////////////////////////////////////////
//  * File:    XConfig.h
//  * Author:  XingFei
//  * Contact: fei.xing@alibaba-inc.com
///////////////////////////////////////////


#ifndef XCONFIG_H_
#define XCONFIG_H_

#include <string>
#include <set>
#include "stdlib.h"
#include "string.h"
#include "stdint.h"

namespace b2bmlr {
	class XConfig
	{
	public:
		struct CfgItem
		{
			std::string m_strKey, m_strValue;
			std::string m_strComments;

			inline bool operator < (const CfgItem &item) const {
				return strcasecmp(m_strKey.c_str(), item.m_strKey.c_str()) < 0;
			}
		};
		typedef std::set<CfgItem> ItemSet;

		struct CfgGroup
		{
			std::string m_strName;
			ItemSet m_items;
			std::string m_strComments;

			inline bool operator < (const CfgGroup &group) const {
				return strcasecmp(m_strName.c_str(), group.m_strName.c_str()) < 0;
			}
		};
		typedef std::set<CfgGroup> GroupSet;

	public:
		std::string m_strLastError;

	public:
		XConfig(void);
		~XConfig(void);

		bool Load(const std::string &strFile);
		bool Store(const std::string &strFile);
		void Clear();

		bool GetItem(const std::string &strGroup, const std::string &strItem, bool &bVal);
		bool GetItem(const std::string &strGroup, const std::string &strItem, int &nVal);
		bool GetItem(const std::string &strGroup, const std::string &strItem, unsigned int &nVal);
		bool GetItem(const std::string &strGroup, const std::string &strItem, double &fVal);
		bool GetItem(const std::string &strGroup, const std::string &strItem, std::string &strVal);
		bool SetItem(const std::string &strGroup, const std::string &strItem, bool &bVal);
		bool SetItem(const std::string &strGroup, const std::string &strItem, int &nVal);
		bool SetItem(const std::string &strGroup, const std::string &strItem, unsigned int &nVal);
		bool SetItem(const std::string &strGroup, const std::string &strItem, double &fVal);
		bool SetItem(const std::string &strGroup, const std::string &strItem, std::string &strVal);
	protected:
		GroupSet m_groups;

	protected:
		bool IsGroupNameLine(const std::string &strLine);
		bool ParseGroupLine(const std::string& strLine, CfgGroup &group);
		bool ParseItemLine(const std::string& strLine, CfgItem &item);

		XConfig::CfgGroup* GetGroup(const std::string &strGroup);
		XConfig::CfgItem* GetItem(const std::string &strGroup, const std::string &strItem);
	};

}	//namespace b2bmlr

#endif
