#include "mlr_trie.hh"
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include <stdarg.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include "mlr_utfconv.h"

#define REMOVE_DEAD_STATES 1

namespace b2bmlr{

const char* DoubleArrayTrie::theTrieMagic = "DATRIE V" DA_TRIE_VERSION;
const char* DADLTrie::theTrieMagic    = "DADLTrie V" DADL_TRIE_VERSION;
const char* DoubleTrie::theTrieMagic  = "DoubleTrie V" DOUBLE_TRIE_VERSION;


#if TRIE_DEBUG
inline void PRINTD(const char* fmt, ...)
{
    if(trie_debug){
        va_list ap;
        va_start (ap, fmt);
        vprintf(fmt, ap);
    }
}
#else
inline void PRINTD(const char* fmt, ...){}
#endif

#if TRIE_DEBUG
#define DUMPD() do { \
    if(trie_debug) dump(); \
} while(0)
#else
#define DUMPD()
#endif

static void stateChange(void* dt, DictStateT from, DictStateT to)
{
    DoubleTrie *trie = (DoubleTrie*)dt;
    trie->relinkState(from, to);
}

bool Dict::write(const char* filename){
    bool ret = true;;
    FILE* fp = fopen(filename, "wb");
    if (!fp){
        fprintf(stderr, "Can not open and write to file: %s\n", filename);
        perror("WriteToFile: ");
        return false;
    }
    ret = write(fp);
    fclose(fp);
    return ret;
}

bool Dict::load(const char* filename){
    FILE* fp = fopen(filename, "rb");
    if (!fp){
        fprintf(stderr, "Can not open and read from file: %s\n", filename);
        perror("LoadFromFile: ");
        return false;
    }
    bool ret = load(fp);
    fclose(fp);
    return ret;
}

DoubleArrayTrie::DoubleArrayTrie()
    : bc(0), bcNeedFree(false)
{
  initKeyStr();
  statsStr = 0;
  stateChangeCallBack = 0;
  stateChangeCallBackData = 0;
  errStr[0] = 0;
}

void DoubleArrayTrie::init()
{
  extend();
}

const char* DoubleArrayTrie::get_err() const {
    return errStr;
}

bool DoubleArrayTrie::checkMagic()
{
    return (strcmp(theTrieMagic, _header.Magic) == 0);
}

void DoubleArrayTrie::setMagic()
{
    strncpy(_header.Magic, theTrieMagic, sizeof(_header.Magic));
}

DoubleArrayTrie::~DoubleArrayTrie()
{
  clear();
}

void DoubleArrayTrie::clear()
{
    if (bc && bcNeedFree) {
        free(bc);
        bc = 0;
        bcNeedFree = false;
    }
    cleanKeyStr();
    if (statsStr != 0){
      free(statsStr);
      statsStr = 0;
    }
    stateChangeCallBack = 0;
    stateChangeCallBackData = 0;
}

char* DoubleArrayTrie::stats()
{
  if(statsStr == NULL){
    statsStr = (char*) malloc(1024);
  }

  int len = 0;
  len += snprintf(statsStr+len, 1024-len, "Trie Stats:\n");
  len += snprintf(statsStr+len, 1024-len, "Trie DA Length: %d\n", getLength());
  len += snprintf(statsStr+len, 1024-len, "base(%d) = %d\n", getBaseState(), base(getBaseState()));
  int n = 0;
  for(int i = 0; i < getLength(); i++){
    if(check(i) > 0) n++;
  }
    
  len += snprintf(statsStr+len, 1024-len, "Number of States: %d\n", n);

  return statsStr;
}

void DoubleArrayTrie::initKeyStr(){
  keyStr = 0;
  keyLens = 0;
  keysSoFar = 0;
  keyStrPOS = 0;
  keyStrMax =0;
  keysMax   = 0;
}

void DoubleArrayTrie::cleanKeyStr(){
  if(keyStr) free(keyStr);
  if(keyLens) free(keyLens);
  initKeyStr();
}

void DoubleArrayTrie::appendKeyStr(const DictKeyT* key, int len){
  if (keysSoFar == keysMax){
    keyLens = (int*)realloc(keyLens,  (keysSoFar + 100) * sizeof(int));
    keysMax = keysSoFar + 100;
  }
  keyLens[keysSoFar++] = len;
  if (keyStrPOS + len >= keyStrMax){
    keyStr = 
      (DictKeyT*)realloc(keyStr, 
			  (keyStrPOS+len+1024)*sizeof(DictKeyT));
    keyStrMax=keyStrPOS + len + 1024;
  }
  for(int i = 0; i < len; i++){
    keyStr[keyStrPOS++] = key[i];
  }
}

bool DoubleArrayTrie::isConsistent(){
  DictKeyT* pos = keyStr;
  for(int i = 0; i < keysSoFar; i++){
    DictKeyT* key = pos;
    int keyLen = keyLens[i];
    if(match(key, keyLen) == STATE_NOT_FOUND){
      return false;
    }
    pos += keyLen;
  }
  
  // make sure for all i > 1 when check(i) == 0, base(i) == 0
  for(int i = 2; i < getLength(); i++){
    if (check(i) == 0){
      assert (base(i) == 0) ;
    }
  }
  return true;
}

DictStateT DoubleArrayTrie::setState(DictStateT s, int ch, DictStateT t)
{
#if STEP_BY_STEP_CHECK
    assert(isConsistent());
#endif
    assert(isValidState(s));
    assert(base(s) > 0 && isFree(nextState(s,ch)));
    DictStateT ns = nextState(s,ch);
    getState(ns);
    check(ns) = s;
    if(t != 0){
        base(ns) = t;
    }
#if STEP_BY_STEP_CHECK
    assert(isConsistent());
#endif
    return t;
}

DictStateT DoubleArrayTrie::getNewState(DictStateT s, int ch){
#if TRIE_DEBUG >= 2
    assert(isConsistent());
#endif
    assert(s>0);
    DictStateT newState = 0;
    if(base(s) > 0 && isFree(nextState(s,ch))){
#if TRIE_DEBUG >= 2
        assert(isConsistent());
#endif
        newState = nextState(s,ch);
    }
    else {
        int largestKey = 0;
        int smallestKey = KEY_MAX;
        // Get all valid state transition from base;
        int keys[KEY_MAX+1];
        int numKeys = getAllKeys(keys, s);

        // Get all valid state transition from base(s)+ch
        int keyt[KEY_MAX+1];
        int numKeyt = 0;

        if ((base(s) > 0) &&
            (check(s) != check(nextState(s,ch)))
            // Don't move the previous transition,
            // it will change the prevstate (s)
            ) {
            numKeyt = getAllKeys(keyt, check(nextState(s,ch)));
        }
         
        if((numKeyt > 0) && (numKeys+1 > numKeyt)){
            int conflictState = nextState(s, ch);
            int stateToMove = check(conflictState);
            largestKey = keyt[numKeyt - 1];
            smallestKey = keyt[0];

            PRINTD("List of keys to move: ");
            for(int i = 0; i<numKeyt; i++){
                PRINTD(" %d ", keyt[i]);
            }
            PRINTD("\n");
            
            int newBase = getNewBase(keyt, numKeyt, smallestKey, largestKey);
	    //	    int newBase2  = DoubleArrayTrie::getNewBase(keyt, numKeyt, smallestKey, largestKey);
	    //	    assert(newBase == newBase2);
            PRINTD("New base: %d\n", newBase);
            
            relocate(stateToMove, newBase);
            assert(check(conflictState) <= 0);
            assert(conflictState == nextState(s,ch));
            newState = conflictState;
        }
        else{
            if (numKeys > 0){
                largestKey = keys[numKeys - 1];
                smallestKey = keys[0];
            }
            
            largestKey = largestKey < ch ? ch : largestKey;
            smallestKey = smallestKey > ch ? ch : smallestKey;
            keys[numKeys++] = ch;
        
            PRINTD("List of keys to move: ");
            for(int i = 0; i<numKeys; i++){
                PRINTD(" %d ", keys[i]);
            }
            PRINTD("\n");
            
            int newBase = getNewBase(keys, numKeys, smallestKey, largestKey);
	    //	    int newBase2 = DoubleArrayTrie::getNewBase(keys, numKeys, smallestKey, largestKey);
	    //	    assert(newBase == newBase2);
            PRINTD("New base: %d\n", newBase);
            
            relocate(s, newBase);
            newState = nextState(s,ch);
            assert(check(newState) <= 0);
        }
    }
    return newState;
}

DictStateT DoubleArrayTrie::match(const DictKeyT* key, int keyLen)
{
    DictStateT b = getBaseState();
    DictStateT next;
    if (b >= getLength()) return STATE_NOT_FOUND;
    for(int i = 0; i < keyLen; i++){
        next = nextState(b, key[i]);
        if (!Check(next, b)){
            return STATE_NOT_FOUND;
        }
        b = next;
    }
    next = nextState(b, KEY_ENDMARKER);
    if (!Check(next, b)) {
        return STATE_NOT_FOUND;
    }
    // printf("BASE[%d] = %d\n",0 next, base(next));

    return base(next);
}

int DoubleArrayTrie::matchAll(const DictKeyT* key, int keyLen,
                              Dict::MatchInfo* matches, int matchLen)
{
    int num = 0;
    DictStateT b = getBaseState();
    DictStateT next;
    if (b < getLength()){
        for(int i = 0; i < keyLen && num < matchLen; i++){
            next = nextState(b, key[i]);
            if (!Check(next, b)) {
                break;
            }
            b = next;
            
            DictStateT s = nextState(b, KEY_ENDMARKER);
            if (Check(s, b)) {
                matches[num].pos = i;
                matches[num++].state = base(s);
            }
        }
    }
    return num;    
}

DictStateT DoubleArrayTrie::add(const DictKeyT* key,
                               int keyLen,
                               DictStateT id) 
{
    DictStateT s = addPartial(key, keyLen);
    DictStateT next = nextState(s, KEY_ENDMARKER);
    if (Check(next, s)) {
        // key exist in the trie
        // set the id there
        base(next) = id;
        return next;
    }
    s = addNewChar(s, KEY_ENDMARKER, id);
    return s;
}

DictStateT DoubleArrayTrie::addPartial(const DictKeyT* key,
                                       int keyLen) 
{
    PRINTD("Add key: %s\n", key);
    PRINTD("Before add\n");
    DUMPD();
    DictStateT s = getBaseState();
    if (s >= getLength()) extend();
    DictStateT next;
    int i = 0;
    for(; i < keyLen && base(s) > 0; i++){
        next = nextState(s, key[i]);
        if (next >= getLength()){
            extend();
        }
        if (!Check(next, s)) {
            break;
        }
        s = next;
    }
    
    // add the rest of the string into the trie
    for(; i < keyLen; i++){
        if (i == 0) { PRINTD("Add leading char: %c\n", key[i]);} 
        s = addNewChar(s, key[i]);
    }

    return s;
}

DictStateT DoubleArrayTrie::addPartialRev(const DictKeyT* key,
                                       int keyLen) 
{
    PRINTD("Add key: %s\n", key);
    PRINTD("Before add\n");
    DUMPD();
    DictStateT s = getBaseState();
    if (s >= getLength()) extend();
    DictStateT next;
    int i = keyLen-1;
    for(; i>=0 && base(s)>0; i--){
        next = nextState(s, key[i]);
        if (next >= getLength()){
            extend();
        }
        if (!Check(next, s)) {
            break;
        }
        s = next;
    }
    
    // add the rest of the string into the trie
    for(; i >= 0; i--){
        s = addNewChar(s, key[i]);
    }
    return s;
}

bool DoubleArrayTrie::write(FILE* fp){
    setMagic();
    int toWrite = 1;
    int written = 0;
    while(toWrite > written){
        written += fwrite(&_header, sizeof(_header), 1, fp);
    }
    // write the base and check array
    toWrite = getLength();
    written = 0;
    while(toWrite > written){
        written += fwrite(bc+written, sizeof(BC), toWrite - written, fp);
    }
    return true;
}

const char *DoubleArrayTrie::toolsVersion( )
{
	return theTrieMagic;
}

const char *DoubleArrayTrie::dictVersion( FILE *fp )
{
    struct stat stat;
    fstat(fileno(fp), &stat);
    size_t fileSz = stat.st_size - ftell(fp);
    if (fileSz < sizeof(_header)){
        snprintf(errStr, sizeof(errStr), "File size too small" );
        return errStr;
    }
    int toRead = 1;
    int read = 0;
    while(toRead > read){
        read += fread(&_header, sizeof(_header), 1, fp);
	}
	return _header.Magic;
}

bool DoubleArrayTrie::load(FILE* fp){
    struct stat stat;
    fstat(fileno(fp), &stat);
    size_t fileSz = stat.st_size - ftell(fp);
    if (fileSz < sizeof(_header)){
        snprintf(errStr, sizeof(errStr), "File size too small" );
        return false;
    }
    int toRead = 1;
    int read = 0;
    while(toRead > read){
        read += fread(&_header, sizeof(_header), 1, fp);
    }
    if(!checkMagic()){
        snprintf(errStr, sizeof(errStr), "Magic check failed" );
        _header.init();
        return false;
    }

    read = 0;
    int length = getLength();
    if(length < 0 || fileSz < (sizeof(_header) + length*sizeof(BC))){
        snprintf(errStr, sizeof(errStr), "File size too small" );
        _header.init();
        return false;
    }
    if(length > 0){
        if(bcNeedFree && bc){
           free(bc);
        }
        bc = (BC*)malloc(length*sizeof(BC));
        bcNeedFree = true;
        // read the base-check array
        toRead = length;
        read = 0;
        while(toRead > read){
            read += fread(bc+read, sizeof(BC), toRead - read, fp);
        }
    }
    else {
        _header.init();
    }
    return true;
}
        
char* DoubleArrayTrie::load(char* mem, size_t len){

    if (len < sizeof(_header)){
        snprintf(errStr, sizeof(errStr), "File size too small" );
        return false;
    }

    memcpy(&_header, mem, sizeof(_header));
    mem += sizeof(_header);
    len -= sizeof(_header);
    if(!checkMagic()){
        snprintf(errStr, sizeof(errStr), "Magic check failed" );
        _header.init();
        return NULL;
    }

    int length = getLength();
    if(length < 0 || len < (length*sizeof(BC))){
        snprintf(errStr, sizeof(errStr), "File size too small" );
        _header.init();
        return NULL;
    }
    if(length > 0){
        if (bc && bcNeedFree){
            free(bc);
            bcNeedFree = false;
        }
        bc = (BC*)mem;
        mem += length*sizeof(BC);
        len -= length*sizeof(BC);
    }
    else {
        _header.init();
        return NULL;
    }
    return mem;
}

void DoubleArrayTrie::listAllEntries(){
    char str[1024];
    listAll(str, 0, getBaseState());
}

void DoubleArrayTrie::listAll(char *ptr, int len, DictStateT s) {
    if(s>0 &&
       base(s) > 0 &&
       base(s)+key2idx(KEY_ENDMARKER) < getLength() &&
       check(base(s)+key2idx(KEY_ENDMARKER)) == s){
        ptr[len] = 0;
        printf("%s\n", ptr);
        /*
        if (base(base(s)+KEY_ENDMARKER)){
            printf("leaf base(%d) = %d\n",
                   base(base(s)+KEY_ENDMARKER),
                   base(s)+KEY_ENDMARKER);
        }
        */
    }
    
    for(int i = key2idx(0); (i < KEY_MAX)&&(s+i <getLength()); i++){
        DictStateT t = base(s)+i;
        if (Check(t, s)){
            ptr[len] = idx2key(i);
            listAll(ptr, len+1, t);
        }
    }
}

void DoubleArrayTrie::listAllEntriesUtf8(){
    char str[1024];
    listAllUtf8(str, 0, getBaseState());
}

void DoubleArrayTrie::listAllUtf8(char *ptr, int len, DictStateT s) {
    if(s>0 &&
       base(s) > 0 &&
       base(s)+key2idx(KEY_ENDMARKER) < getLength() &&
       check(base(s)+key2idx(KEY_ENDMARKER)) == s){
        if ((len) & 1){
            printf("Something fishy here\n");
        }
        ptr[len] = 0;
        char u8buf[1024];
        int u8len = mlr_utf16_to_utf8((uint16_t*)ptr, (len)/2, (uint8_t*)u8buf, 1024);
        if (u8len > 0){
            u8buf[u8len] = 0;
            printf("%s\n", u8buf);
        }
        else {
            printf("Something is completely wrong\n");
        }
    }
    
    for(int i = key2idx(0); (i < KEY_MAX)&&(s+i <getLength()); i++){
        DictStateT t = base(s)+i;
        if (Check(t, s)){
            ptr[len] = idx2key(i);
            listAllUtf8(ptr, len+1, t);
        }
    }
}

bool DoubleArrayTrie::extend(){
    int delta = 10 * KEY_MAX;
    delta = delta > 4*1024 ? delta : 4*1024;
    int length = getLength();
    int newLength = length + delta;
    if (bcNeedFree){
        bc = (BC*)realloc(bc, newLength * sizeof(BC));
    }
    else {
        BC* tmpBC = (BC*)malloc(newLength * sizeof(BC));
        memcpy(tmpBC, bc, length*sizeof(BC));
        bc = tmpBC;
        bcNeedFree = true;
    }
    // FATAL: not enough memory
    assert(bc != NULL);

    memset(bc+length, 0, (newLength - length)*sizeof(BC));
    setLength(newLength);
    if (length == 0) {
        base(1) = 1;
    }
    
    return true;
}

  
/* Move base for state s to new location beginning at b */
void DoubleArrayTrie::relocate(DictStateT s, int b)
{
    int numKeys;
    int keys[KEY_MAX+1];
#if STEP_BY_STEP_CHECK
    assert(isConsistent());
#endif

    numKeys = getAllKeys(keys, s);
    for(int i = 0; i < numKeys; i++){
        int c = keys[i];
        DictStateT t = nextState(s,c);
        DictStateT ns = b+key2idx(c);
	getState(ns);
        check(ns) = s;
        base(ns) = base(t);
        if(stateChangeCallBack){
            (*stateChangeCallBack)(stateChangeCallBackData, t, ns);
        }
        int keys2[KEY_MAX+1];
        int numKeys2 = getAllKeys(keys2, t);
        for(int j = 0; j < numKeys2; j++){
            int c2 = keys2[j];
            check(nextState(t,c2)) = ns;
        }
	freeState(t);
    }
    base(s) = b; 

#if STEP_BY_STEP_CHECK
    assert(isConsistent());
#endif
}

DictStateT DoubleArrayTrie::getNewBase(int keys[], int numKeys,
                             int smallestKey, int largestKey)
{
    DictStateT i = 1;
    bool found = false;
    while(!found){
        i++;
        if (i == getLength() - key2idx(largestKey)) extend();
        if (check(key2idx(smallestKey) + i) <= 0) {
            // check for all keys
            found = true;
            for(int j = 0; j < numKeys; j++){
                if(check(i+key2idx(keys[j])) > 0){
                    found = false;
                    break; 
                }
            }
        }
    } 
    return i;
}


DictStateT DoubleArrayTrie::addNewChar(DictStateT s, int ch, DictStateT id)
{
#if STEP_BY_STEP_CHECK
    assert(isConsistent());
#endif
    assert(isValidState(s));
    if(base(s) > 0 && nextState(s,ch) < getLength() ){
      if(check(nextState(s,ch)) == s){
         listAllEntries();
      }
      assert(check(nextState(s,ch)) != s);
    }
#if STEP_BY_STEP_CHECK
    assert(isConsistent());
#endif
    DictStateT newState = getNewState(s, ch);
    assert(base(s) > 0);
    assert(newState == nextState(s,ch));
    assert(check(nextState(s, ch)) <= 0);
    //    if(ch != KEY_ENDMARKER){
    //        setState(s, ch, -nextState);
    //    }
    //    else {
    setState(s, ch, id);
    //    }

#if STEP_BY_STEP_CHECK
    assert(isConsistent());
#endif
    return newState;
}


DictStateT DoubleArrayTrie::matchLongest(const DictKeyT* key, int keyLen, int& matchedLen){
    DictStateT b = getBaseState();
    DictStateT next;
    int length = getLength();
    if (b >= length) return STATE_NOT_FOUND;
    int i = 0;
    for(i = 0; i < keyLen; i++){
        next = nextState(b, key[i]);
        if (!Check(next, b)) {
            break;
        }
        b = next;
    }
    matchedLen = i;
    return b;
}

DictStateT DoubleArrayTrie::matchReverse(const DictKeyT* key, int keyLen, DictStateT initState, int &matchedLen){
    // check(t) = s
    // base(s) + (ch+1) = t
    // ch = t - base(s) - 1;
    DictStateT t = initState;
    int i = 0;
    while(t != getBaseState() && i < keyLen){
        DictStateT s = check(t);
        if (nextState(s, key[i]) != t) break;
        t = s;
        i++;
    }
    matchedLen = i;
    return t;
}

// debug helpers
void DoubleArrayTrie::printTrie(DictStateT s){
    int keys[KEY_MAX+1];
    int numKeys = getAllKeys(keys, s);
    if (numKeys == 0){
        printLink(s);
    }
    else{
        for(int i = 0; i < numKeys; i++){
            printTrie(nextState(s, keys[i]));
        }
    }
}

void DoubleArrayTrie::printLink(DictStateT s){
    char str[1024];
    int  state[1024];
    int i = 0;
    while(s != getBaseState()){
        char ch = getKeyRev(s);
        str[i] = ch;
        state[i] = s;
        s = check(s);
        i++;
    }
    printf("[1]");
    while(i-- >= 0){
            printf("%c[%d]", str[i], state[i]);
    }
    printf("\n");
}

bool DADLTrie::checkMagic()
{
    return (strcmp(theTrieMagic, _header.Magic) == 0);
}

void DADLTrie::setMagic()
{
    strncpy(_header.Magic, theTrieMagic, sizeof(_header.Magic));
}

void DADLTrie::getState(DictStateT t){
  DictStateT prev = -base(t);
  DictStateT next = -check(t);
  check(prev) = -next;
  base(next) = -prev;
  base(t) = check(t) = 0;
}

void DADLTrie::freeState(DictStateT t){
  DictStateT s = -check(0);
  while((s != 0) && (s < t)){
    s = -check(s);
  }
  
  DictStateT prev = -base(s);
  base(t) = -prev;
  base(s) = -t;
  check(prev) = -t;
  check(t) = -s;
}

bool DADLTrie::check_list(){
  int ret = true;
  int n = 0;
  for(int i = 0; i < getLength(); i++){
    if(base(i) < 0){
      n++;
      if (check(-base(i)) != base(-check(i))){
	printf("Attention!!!\n");
	ret = false;
      }
    }
    if ((check(i) == 0) && (i != 1) && (i != -base(0))){
      printf("Potential lose of cell# %d\n", i);
      n++;
    }
    if(check(i) < 0){
      if (check(-base(i)) != base(-check(i))){
	printf("Attention!!!\n");
	ret = false;
      }
    }
  }
  
  int i = 0;
  while(check(i) < 0){
    n--;
    if (i >= -check(i)){ ret = false; }
    i = -check(i);
  }
  i = 2;
  while(base(i) < 0){
    if( i <= -base(i)) { ret = false; }
    i = -base(i);
  }
  static bool lost = false;
  if(n != 0){
    lost = true;
    printf("Lost cells: %d\n", n);
    for(int j = 0; j < getLength(); j++){
      printf("%6d | %6d | %6d\n", j, base(j), check(j));
    }
    assert(!"Lost cells!!\n");
        }
  else if (lost){
    printf("We got them back!!\n");
  }
  return ret;
}

bool DADLTrie::extend(){
  int delta = 10 * KEY_MAX;
  delta = delta > 4*1024 ? delta : 4*1024;
  int length = getLength();
  int newLength = length + delta;
  if (bcNeedFree){
      bc = (BC*)realloc(bc, newLength * sizeof(BC));
  }
  else {
      BC* tmpBC = (BC*)malloc(newLength * sizeof(BC));
      memcpy(tmpBC, bc, length*sizeof(BC));
      bc = tmpBC;
      bcNeedFree = true;
  }
  
  // FATAL: not enough memory
  assert(bc != NULL);
  
  memset(bc+length, 0, (newLength - length)*sizeof(BC));
  setLength(newLength);
  
  if (length == 0) {
    base(1) = 1;
    
    // set up the free lists
    check(0) = -2;
    base(2) = 0;
    for(int i = 2; i < newLength-1; i++){
      check(i) = -(i+1);
      base(i+1) = -i;
    }
    check(newLength-1) = 0;
    base(0) = -(newLength-1);
  }
  else{
    // add new cells into the free lists
    int tail = - base(0);
    check(tail) = -length;
    base(length) = -tail;
    for(int i = length; i < newLength-1; i++){
      check(i) = -(i+1);
      base(i+1) = -i;
    }
    check(newLength-1) = 0;
    base(0) = -(newLength-1);
  }
  return true;
} 

DictStateT DADLTrie::getNewBase(int keys[], int numKeys,
			 int smallestKey, int largestKey)
{
  DictStateT i = 0;
  bool found = false;
  while((!found)){
    assert(i>=0);
    if(check(i) == 0) extend();
    i = -check(i);
    if (i < smallestKey + 2) continue;
    if (i >= getLength() - (largestKey - smallestKey)) extend();
    
    // check for all keys
    found = true;
    for(int j = 0; j < numKeys; j++){
      if(check(i+(keys[j] - smallestKey)) > 0){
	found = false;
	break; 
      }
    }
  } 
  
  return idx2key(i - smallestKey);
}

void DADLTrie::init()
{
  extend();
}

void DADLTrie::clear()
{
    if (bc && bcNeedFree) {
        free(bc);
        bc = 0;
        bcNeedFree = false;
    }
    cleanKeyStr();
    if (statsStr != 0){
      free(statsStr);
      statsStr = 0;
    }
}

DoubleTrie::~DoubleTrie(){
    clear();
}

const char* DoubleTrie::get_err() const {
    return errStr;
}

void DoubleTrie::init(){
    _left.init();
    _right.init();
    _header.init();
    if(_sepStates && _sepStatesNeedFree) {
        free(_sepStates);
        _sepStates = 0;
        _sepStatesNeedFree = false;
    }
    _right.SetStateChangeCallBack(&stateChange, this);
    errStr[0] = 0;
}

void DoubleTrie::clear(){
    _left.clear();
    _right.clear();
    _header.init();
    if(_sepStates && _sepStatesNeedFree) {
        free(_sepStates);
        _sepStates = 0;
        _sepStatesNeedFree = false;
    }
}

// Add by Rubin Xiang to print version
const char *DoubleTrie::toolsVersion( )
{
	return theTrieMagic;
}

const char *DoubleTrie::dictVersion( FILE *fp)
{
    struct stat stat;
    fstat(fileno(fp), &stat);
    size_t fileSz = stat.st_size - ftell(fp);
    if (fileSz < sizeof(_header)){
        snprintf(errStr, sizeof(errStr), "File size too small" );
        return errStr;
    }
    int toRead = 1;
    int read = 0;
    while(toRead > read){
        read += fread(&_header, sizeof(_header), 1, fp);
	}
	return _header.Magic;
}

bool DoubleTrie::load(FILE* fp){
    struct stat stat;
    fstat(fileno(fp), &stat);
    size_t fileSz = stat.st_size - ftell(fp);
    if (fileSz < sizeof(_header)){
        snprintf(errStr, sizeof(errStr), "File size too small" );
        return false;
    }
    int toRead = 1;
    int read = 0;
    while(toRead > read){
        read += fread(&_header, sizeof(_header), 1, fp);
    }
    if (!checkMagic()){
        snprintf(errStr, sizeof(errStr), "Magic check failed" );
        _header.init();
        return false;
    }
    if (_header.sepStatesCap < 0 || fileSz < (sizeof(_header) + _header.sepStatesCap*sizeof(SeparateState))){
        snprintf(errStr, sizeof(errStr), "File size too small" );
        _header.sepStatesCap = 0;
        return false;
    }
    if(_header.sepStatesCap > 0){
        if(_sepStates && _sepStatesNeedFree) free(_sepStates);
        _sepStates = (SeparateState*)malloc(_header.sepStatesCap*sizeof(SeparateState));
        _sepStatesNeedFree = true;
        // read the linkage array
        toRead = _header.sepStatesCap;
        read = 0;
        while(toRead > read){
            read += fread(_sepStates+read, sizeof(SeparateState), toRead - read, fp);
        }
    }
    if(!_left.load(fp)){
        snprintf(errStr, sizeof(errStr), "Left trie load failed: %s", _left.get_err());
        return false;
    }
    else if(!_right.load(fp)){
        snprintf(errStr, sizeof(errStr), "Rigth trie load failed: %s", _left.get_err());
        return false;
    }
    return true;
}

char* DoubleTrie::load(char* mem, size_t len){
    if (len < sizeof(_header)){
        snprintf(errStr, sizeof(errStr), "File size too small" );
        return false;
    }
    memcpy(&_header, mem, sizeof(_header));
    mem += sizeof(_header);
    len -= sizeof(_header);

    if (!checkMagic()){
        snprintf(errStr, sizeof(errStr), "Magic check failed" );
        _header.init();
        return NULL;
    }

    if (_header.sepStatesCap < 0 || len < ( _header.sepStatesCap*sizeof(SeparateState))){
        snprintf(errStr, sizeof(errStr), "File size too small" );
        _header.sepStatesCap = 0;
        return NULL;
    }

    if(_header.sepStatesCap > 0){
        if(_sepStates && _sepStatesNeedFree){
            free(_sepStates);
            _sepStatesNeedFree = false;
        }
        _sepStates = (SeparateState*)mem;
        mem += _header.sepStatesCap * sizeof(SeparateState);
        len -= _header.sepStatesCap * sizeof(SeparateState);
    }

    char* tmpMem = _left.load(mem, len);
    if (tmpMem != NULL){
        len -= (tmpMem - mem);
        mem = tmpMem;
        tmpMem =  _right.load(mem, len);
        if(!tmpMem){
            snprintf(errStr, sizeof(errStr), "Right trie load failed: %s", _left.get_err());
        }
    }
    else {
        snprintf(errStr, sizeof(errStr), "Left trie load failed: %s", _left.get_err());
    }

    return tmpMem;
}

bool DoubleTrie::write(FILE* fp){
    setMagic();
    int toWrite = 1;
    int written = 0;
    while(toWrite > written){
        written += fwrite(&_header, sizeof(_header), 1, fp);
    }
    toWrite = _header.sepStatesCap; // only data_used is actually required to be written
    written = 0;
    while(toWrite > written){
        written += fwrite(_sepStates+written, sizeof(SeparateState), toWrite - written, fp);
    }
    _left.write(fp);
    _right.write(fp);
    
    return true;
}

DictStateT DoubleTrie::match(const DictKeyT* key, int keyLen){
    DictStateT ret = STATE_NOT_FOUND;
    int lMatched = 0;
    // matchLongest returns < 0 state if it is a seperate state
    DictStateT s = _left.matchLongest(key, keyLen, lMatched);
    DictStateT leftSep = _left.base(s);
    if (isSepState(leftSep)) {
        DictStateT rightState = getLinkState(leftSep);
        int rMatched = 0;
        DictStateT rightMatchState = _right.matchReverse(key+lMatched, keyLen-lMatched, rightState, rMatched);
        if(rightMatchState == _right.getBaseState() && rMatched == keyLen-lMatched){
            // find a match in the right state
            ret = linkStateGetId(leftSep);
        }
    }
    else if(lMatched == keyLen){
        DictStateT t = _left.getNextState(s, KEY_ENDMARKER);
	if ( _left.Check(t, s)){
            ret = _left.base(t);
        }
    }
    return ret;
}
int DoubleTrie::matchAll(const DictKeyT* key, int keyLen,
                         MatchInfo* matches, int matchLen){
    int num = 0;
    DictStateT b = _left.getBaseState();
    DictStateT next;
    int leftLen = _left.getLength();
    if (b < leftLen){
        int i;
        for(i = 0; i < keyLen && num < matchLen; i++){
            next = _left.nextState(b, key[i]);
            if (!_left.Check(next, b)) {
                break;
            }
            else if(i < keyLen) {
                DictStateT leftSep = _left.base(next);
                if (isSepState(leftSep)) {
                    DictStateT rightState = getLinkState(leftSep);
                    int rMatched = 0;
                    if (i < keyLen -1){
                        rightState = _right.matchReverse(key+i+1, keyLen-i-1, rightState, rMatched);
                    }
                    if(rightState == _right.getBaseState()){
                        // find a match in the right state
                        matches[num].pos =  i+rMatched;
                        matches[num++].state = linkStateGetId(leftSep);
                    }
                    break;
                }
            }
            b = next;
            
            DictStateT s = _left.nextState(b, KEY_ENDMARKER);
            if ( _left.Check(s, b)) {
                matches[num].pos = i;
                matches[num++].state = _left.base(s);
            }
        }
    }
    return num;    
}

void DoubleTrie::unlinkState(DictStateT l){
    assert(l < 0 && -l < _header.sepStatesCap);
#if USE_MAP
    DictStateT right = getLinkState(l);
    assert(_sepStatesReverseMap[right][l] == 1);
    if(_sepStatesReverseMap[right].size() == 1){
        _sepStatesReverseMap.erase(right);
    }
    else{
        _sepStatesReverseMap[right].erase(l);
    } 
#endif
    _sepStates[-l].right = _sepStates[0].right;
    _sepStates[0].right = l;
    _sepStates[-l].id = 0;
    _sepStates[-_sepStates[-l].right].id = l;
}

DictStateT DoubleTrie::getNewSepState(){
    if(_header.sepStatesCap <= 0 || _sepStates[0].right == 0){
#define EXPAND_CHUNK_LEN 1024
        // expand the _sepStates array
        _sepStates = (SeparateState*)realloc(_sepStates, sizeof(SeparateState)*(_header.sepStatesCap + EXPAND_CHUNK_LEN));
        assert(_sepStates != NULL);
        int start = _header.sepStatesCap;
        if (_header.sepStatesCap == 0){
            _sepStates[0].right = 0;
            _sepStates[0].id = 0;
            start = 2;
        }

        for(int j = start; j < _header.sepStatesCap+EXPAND_CHUNK_LEN-1; j++){
            _sepStates[j].right = -(j+1);
            _sepStates[j].id = -(j-1); 
        }
        _sepStates[_header.sepStatesCap+EXPAND_CHUNK_LEN-1].right = 0;
        _sepStates[start].id    = 0;
        _sepStates[0].id = -(_header.sepStatesCap+EXPAND_CHUNK_LEN-1);
        _sepStates[0].right = -start;
        _header.sepStatesCap += EXPAND_CHUNK_LEN;
    }
    int s = _sepStates[0].right;
    _sepStates[0].right = _sepStates[-s].right;
    _sepStates[-_sepStates[0].right].id = 0;
    return s;
}

DictStateT DoubleTrie::add(const DictKeyT* key,
                           int keyLen,
                           DictStateT id){
    assert(id > 0);
    // search the key first
    // case 1: failed on left trie
    //   Make a new separate state
    //   Add the remaining string to the RH trie
    //   Link the two tries
    // case 2: failed on right trie
    //   Add the matched string in the R trie to the L trie
    //   Make two new separate states
    //   Add two remaining strings to the R trie
    //   Link both tries
    
    int lMatched = 0;
    DictStateT left_s = _left.matchLongest(key, keyLen, lMatched);

    DictStateT leftSep = _left.base(left_s);
    
    if (!isSepState(leftSep)){
        if(lMatched == keyLen){
            // found the whole key
            // Is the end marker there?
            DictStateT s = _left.getNextState(left_s, KEY_ENDMARKER);
            if (s != STATE_NOT_FOUND){
                // set the new value
                _left.base(s) = id;
            }
            else {
               _left.addNewChar(left_s, KEY_ENDMARKER, id);
            }
        }
        else {
            // get a new sepState and insert an extra char into the left tire
            // and insert the rest into the rigth trie
            DictStateT sepState = getNewSepState();
            _left.addNewChar(left_s, key[lMatched], sepState);
            DictStateT right_s = _right.addPartialRev(key+lMatched+1, keyLen-lMatched-1);
            linkState(sepState, right_s);
            linkStateSetId(sepState, id);
        }
    }
    else {
        // continue search into the right trie
        DictStateT rightState = getLinkState(leftSep);
        int rMatched = 0;
        DictStateT rightMatchState = _right.matchReverse(key+lMatched, keyLen-lMatched, rightState, rMatched);
        if(rightMatchState == _right.getBaseState() && rMatched == keyLen-lMatched){
            // find a match in the right state
            linkStateSetId(leftSep, id);
        }
        else {
            // insert the matched part into the left trie
            // 1. save the original state
            DictStateT oldSepState = leftSep;
            DictStateT leftState = _left.addPartial(key, lMatched+rMatched);
            int rKey = _right.getKeyRev(rightMatchState);
            DictStateT oldRightState = _right.check(rightMatchState);
            if (rKey != KEY_ENDMARKER){
                _left.addNewChar(leftState, rKey, oldSepState);
                linkState(oldSepState, oldRightState);
            }
            else {
                _left.addNewChar(leftState, KEY_ENDMARKER, linkStateGetId(oldSepState));
                unlinkState(oldSepState);
            }
#if REMOVE_DEAD_STATES
            // FIXME: 
            // 2. remove the useless states in the right trie
            //   if the first char is not a leaf node or it has inbound pointer
            //          done
            //   otherwise it must be the string just reshuffled, delete it and
            //   move backward; while the state has no child and no inbound pointer
            //          remove it
            DictStateT s = rightState;
            int keys[KEY_MAX+1];
            int numKeys = _right.getAllKeys(keys, s);
            if (numKeys == 0){
                for(int i = 0; i < rMatched && s != _right.getBaseState(); i++){
                    DictStateT b = _right.check(s);
                    if (!hasInLinks(s) && b>0){
                        int keys[KEY_MAX+1];
                        int numKeys = _right.getAllKeys(keys, b);
                        if (numKeys == 1){
                            assert(keys[0] == key[lMatched+i]);
                            // no children and no inLinks;
                            _right.freeState(s);
                        }
                        else {
                            break;
                        }
                    }
                    else{
                        break;
                    }
                    s = b;
                }
            }
#endif
            // 3. insert the rest of the string into the right trie
            if (lMatched+rMatched < keyLen){
                DictStateT sepState = getNewSepState();
                _left.addNewChar(leftState, key[lMatched+rMatched], sepState);
                DictStateT right_s = _right.addPartialRev(key+lMatched+rMatched+1, keyLen-lMatched-rMatched-1);
                linkState(sepState, right_s);
                linkStateSetId(sepState, id);
            }
            else {
                _left.addNewChar(leftState, KEY_ENDMARKER, id);
            }
        }        
    }

    return id;
}

char* DoubleTrie::stats()
{
    /* FIXME: */
    return 0;
}

void DoubleTrie::listAllEntries()
{
    char str[1024];
    listAll(str, 0, _left.getBaseState());
}

void DoubleTrie::listAll(char *ptr, int len, DictStateT s) {
    if(s>0){
        if(_left.base(s) > 0 &&
           _left.base(s)+_left.key2idx(KEY_ENDMARKER) < _left.getLength() &&
           _left.check(_left.base(s)+_left.key2idx(KEY_ENDMARKER)) == s){
            ptr[len] = 0;
            printf("%s\n", ptr);
        }
        else {
            DictStateT leftSep = _left.base(s);
            if (isSepState(leftSep)){
                DictStateT rightState = getLinkState(leftSep);
                int rLen =_right.getEntryRev((DictKeyT*)(ptr+len), rightState);
                if(rLen >= 0){
                    ptr[len+rLen] = 0;
                    printf("%s\n", ptr);                
                }
            }
        }
    }

    
    for(int i = _left.key2idx(0); (i < KEY_MAX)&&(s+i <_left.getLength()); i++){
        DictStateT t = _left.base(s)+i;
        if ((t > 0) && (_left.check(t) == s)){
            ptr[len] = _left.idx2key(i);
            listAll(ptr, len+1, t);
        }
    }
}

void DoubleTrie::listAllEntriesUtf8()
{
    char str[1024];
    listAllUtf8(str, 0, _left.getBaseState());
}

void DoubleTrie::listAllUtf8(char *ptr, int len, DictStateT s) {
    if(s>0){
        if(_left.base(s) > 0 &&
           _left.base(s)+_left.key2idx(KEY_ENDMARKER) < _left.getLength() &&
           _left.check(_left.base(s)+_left.key2idx(KEY_ENDMARKER)) == s){
            ptr[len] = 0;
            if ((len) & 1){
                printf("Something fishy here\n");
            }
            char u8buf[1024];
            int u8len = mlr_utf16_to_utf8((uint16_t*)ptr, len/2, (uint8_t*)u8buf, 1024);
            if (u8len > 0){
                u8buf[u8len] = 0;
                printf("%s\n", u8buf);
            }
            else {
                printf("Something is completely wrong\n");
            }
        }
        else {
            DictStateT leftSep = _left.base(s);
            if (isSepState(leftSep)){
                DictStateT rightState = getLinkState(leftSep);
                int rLen =_right.getEntryRev((DictKeyT*)(ptr+len), rightState);
                if(rLen >= 0){
                    if ((len+rLen) & 1){
                        printf("Something fishy here\n");
                    }
                    ptr[len+rLen] = 0;
                    char u8buf[1024];
                    int u8len = mlr_utf16_to_utf8((uint16_t*)ptr, (len+rLen)/2, (uint8_t*)u8buf, 1024);
                    if (u8len > 0){
                        u8buf[u8len] = 0;
                        printf("%s\n", u8buf);
                    }
                    else {
                        printf("Something is completely wrong\n");
                    }
                }
            }
        }
    }

    
    for(int i = _left.key2idx(0); (i < KEY_MAX)&&(s+i <_left.getLength()); i++){
        DictStateT t = _left.base(s)+i;
        if ((t > 0) && (_left.check(t) == s)){
            ptr[len] = _left.idx2key(i);
            listAllUtf8(ptr, len+1, t);
        }
    }
}


bool DoubleTrie::checkMagic()
{
    return (strcmp(theTrieMagic, _header.Magic) == 0);
}

void DoubleTrie::setMagic()
{
    strncpy(_header.Magic, theTrieMagic, sizeof(_header.Magic));
}

}
