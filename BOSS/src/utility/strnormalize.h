#ifndef __STRNORMALIZE_H__
#define __STRNORMALIZE_H__

#include <string>
#include "stdlib.h"
#include "string.h"
#include "stdint.h"

namespace b2bmlr {

const unsigned SNO_TO_LOWER      = 1;
const unsigned SNO_TO_UPPER      = 2;
const unsigned SNO_TO_HALF       = 4;
const unsigned SNO_TO_SIMPLIFIED = 8;

void strnormalize_gbk(char *text, unsigned options
		= SNO_TO_LOWER | SNO_TO_HALF);
void strnormalize_gbk(std::string &text, unsigned options
		= SNO_TO_LOWER | SNO_TO_HALF);
void strnormalize_utf8(char *text, unsigned options
		= SNO_TO_LOWER | SNO_TO_HALF);
void strnormalize_utf8(std::string &text, unsigned options
		= SNO_TO_LOWER | SNO_TO_HALF);

const std::string gbk_to_utf8(const std::string &text);
const std::string utf8_to_gbk(const std::string &text);
bool is_gbk(unsigned short code);

struct hash_c_string // hash c string to uint32_t
{
	hash_c_string() {}

	uint32_t operator ()(const char *text) const
	{
		static const uint32_t primes[16] =
		{
			0x01EE5DB9, 0x491408C3, 0x0465FB69, 0x421F0141,
			0x2E7D036B, 0x2D41C7B9, 0x58C0EF0D, 0x7B15A53B,
			0x7C9D3761, 0x5ABB9B0B, 0x24109367, 0x5A5B741F,
			0x6B9F12E9, 0x71BA7809, 0x081F69CD, 0x4D9B740B,
		};
		uint32_t sum = 0;
		for (size_t i = 0; text[i]; i ++)
			sum += primes[i & 15] * (unsigned char)text[i];
		return sum;
	}
};

struct hash_string // hash c string to uint32_t
{
	hash_string() {}

	uint32_t operator ()(const std::string &text) const
	{
		static const uint32_t primes[16] =
		{
			0x01EE5DB9, 0x491408C3, 0x0465FB69, 0x421F0141,
			0x2E7D036B, 0x2D41C7B9, 0x58C0EF0D, 0x7B15A53B,
			0x7C9D3761, 0x5ABB9B0B, 0x24109367, 0x5A5B741F,
			0x6B9F12E9, 0x71BA7809, 0x081F69CD, 0x4D9B740B,
		};
		uint32_t sum = 0;
		for (size_t i = 0; i != text.size(); i ++)
			sum += primes[i & 15] * (unsigned char)text[i];
		return sum;
	}
};

} // namespace b2bmlr

#endif /* __STRNORMALIZE_H__ */
