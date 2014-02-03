#include <stdio.h>
#include <string.h>
#include <assert.h>
#include <ctype.h>
#include <stdlib.h>
#include "StringHash.h"

namespace b2bmlr {

	unsigned int hashString(const char *szStr, unsigned int dwHashType)
	{
		if (szStr == NULL) 
			return 0;
		unsigned int cKey;
		unsigned char *key = (unsigned char *)szStr;
		unsigned int seed1 = 0x7FED7FED, seed2 = 0xEEEEEEEE;

		while (*key != 0) {
			cKey = *key++;
			seed1 = CRYPT_TABLE[(dwHashType << 8) + cKey] ^ (seed1 + seed2);
			seed2 = cKey + seed1 + seed2 + (seed2 << 5) + 3;
		}

		return seed1;
	}
	unsigned long hashString64(const char *szStr)
	{
		unsigned long h = hashString(szStr, 0);
		h <<= 32;
		h |= hashString(szStr, 1);
		return h;
	}
    

}

