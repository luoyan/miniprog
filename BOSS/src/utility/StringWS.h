///////////////////////////////////////////
//  * File:    StringWS.h
//  * Author:  XingFei
//  * Contact: fei.xing@alibaba-inc.com
///////////////////////////////////////////

#ifndef __STRINGWS_H__
#define __STRINGWS_H__

namespace b2bmlr {

struct StringWS
{
	std::string m_str;
	std::vector<int> m_vecWordIds;
	std::vector<int> m_vecWordTypes;
};

} // namespace b2bmlr

#endif // __STRINGWS_H__
