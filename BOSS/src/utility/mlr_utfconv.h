#ifndef MLR_UTFCONV_H_
#define MLR_UTFCONV_H_
#include <inttypes.h>

namespace b2bmlr{
/* temporarily put the utf8 <-> utf16 helper functions here */
#define UNI_SUR_HIGH_START      (uint32_t)0xD800
#define UNI_SUR_HIGH_END        (uint32_t)0xDBFF
#define UNI_SUR_LOW_START       (uint32_t)0xDC00
#define UNI_SUR_LOW_END         (uint32_t)0xDFFF

static const int halfShift      = 10; /* used for shifting by 10 bits */

static const uint32_t halfBase     = 0x0010000UL;
static const uint32_t halfMask     = 0x3FFUL;


static  const char trailingBytesForUTF8[256] = {
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
    2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2, 3,3,3,3,3,3,3,3,4,4,4,4,5,5,5,5
};

static const uint32_t offsetsFromUTF8[6] = {
    0x00000000UL, 0x00003080UL, 0x000E2080UL,
    0x03C82080UL, 0xFA082080UL, 0x82082080UL
};

static const uint8_t firstByteMark[7] = {
    0x00, 0x00, 0xC0, 0xE0, 0xF0, 0xF8, 0xFC
};

/*
 * Utility routine to tell whether a sequence of bytes is legal UTF-8.
 * This must be called with the length pre-determined by the first byte.
 * If not calling this from ConvertUTF8to*, then the length can be set by:
 *      length = trailingBytesForUTF8[*source]+1;
 * and the sequence is illegal right away if there aren't that many bytes
 * available.
 * If presented with a length > 4, this returns false.  The Unicode
 * definition of UTF-8 goes up to 4-byte sequences.
 */

static bool isLegalUTF8(const uint8_t *source, int length)
{
    uint8_t a;
    const uint8_t *srcptr = source+length;
    switch (length) {
      default: return false;
        /* Everything else falls through when "true"... */
      case 4: if ((a = (*--srcptr)) < 0x80 || a > 0xBF) return false;
      case 3: if ((a = (*--srcptr)) < 0x80 || a > 0xBF) return false;
      case 2: if ((a = (*--srcptr)) > 0xBF) return false;
        switch (*source) {
            /* no fall-through in this inner switch */
          case 0xE0: if (a < 0xA0) return false; break;
          case 0xF0: if (a < 0x90) return false; break;
          case 0xF4: if (a > 0x8F) return false; break;
          default:  if (a < 0x80) return false;
        }
      case 1: if (*source >= 0x80 && *source < 0xC2) return false;
        if (*source > 0xF4) return false;
    }
    return true;
}

inline int mlr_utf8_to_utf16(const uint8_t *src, size_t src_len, 
                             uint16_t *dest, size_t dest_len)
{
    const uint8_t *srcEnd = src + src_len;
    uint16_t *destStart = dest;
    uint16_t *destEnd = dest + dest_len;
    while (src < srcEnd && dest < destEnd){
        uint32_t ch = *src;
        unsigned short extraBytesToRead = trailingBytesForUTF8[ch];
        if (src + extraBytesToRead >= srcEnd) break;
        if (isLegalUTF8(src, extraBytesToRead+1)){
            ch = 0;
            switch (extraBytesToRead) {
              case 3: ch += *src++; ch <<= 6;
              case 2: ch += *src++; ch <<= 6;
              case 1: ch += *src++; ch <<= 6;
              case 0: ch += *src++;
            }
            ch -= offsetsFromUTF8[extraBytesToRead];
        }
        else return -1;
        if (ch < 0x10000){
            *dest++ = ch;
        }
        else if (dest + 2 < destEnd){
            *dest++ = 0xD800 | (((ch) >> 10) - 0x0040);
            *dest++ = 0xDC00 | ((ch) & 0x03FF);
        } 
        else { return -1; }
    } 
    return dest - destStart;
}

inline int mlr_utf16_to_utf8(const uint16_t* src, size_t srcLen,
                      uint8_t* dest, size_t destLen)
{
    const uint16_t *srcEnd = src + srcLen;
    uint8_t* destStart = dest;
    uint8_t* destEnd = dest + destLen;
    
    while (src < srcEnd){
        uint32_t ch;
        unsigned short bytesToWrite = 0;
        const uint32_t byteMask = 0xBF;
        const uint32_t byteMark = 0x80;
        
        ch = *src++;
        /* If we have a surrogate pair, convert to UTF32 first. */
        if (ch >= UNI_SUR_HIGH_START && ch <= UNI_SUR_HIGH_END &&
            src < srcEnd) {
            uint32_t ch2 = *src;
            if (ch2 >= UNI_SUR_LOW_START && ch2 <= UNI_SUR_LOW_END) {
                ch = ((ch - UNI_SUR_HIGH_START) << halfShift)
                    + (ch2 - UNI_SUR_LOW_START) + halfBase;
                ++src;
            } else {
                return -1;
            }
        } else if (ch >= UNI_SUR_LOW_START && ch <= UNI_SUR_LOW_END) {
            return -1;
        }
        /* Figure out how many bytes the result will require */
        if (ch < (uint32_t)0x80) {                 bytesToWrite = 1;
        } else if (ch < (uint32_t)0x800) {         bytesToWrite = 2;
        } else if (ch < (uint32_t)0x10000) {       bytesToWrite = 3;
        } else if (ch < (uint32_t)0x200000) {      bytesToWrite = 4;
        } else { return -1;
        }
        dest += bytesToWrite;
        if (dest < destEnd){
            switch (bytesToWrite) { /* note: everything falls through. */
              case 4:     *--dest = (ch | byteMark) & byteMask; ch >>= 6;
              case 3:     *--dest = (ch | byteMark) & byteMask; ch >>= 6;
              case 2:     *--dest = (ch | byteMark) & byteMask; ch >>= 6;
              case 1:     *--dest =  ch | firstByteMark[bytesToWrite];
            }
            dest += bytesToWrite;
            
        }
        else {
            return -1;
        }
    }
    return dest - destStart;
}

} //namespace b2bmlr
#endif /* YWS_UTFCONV_H_ */
