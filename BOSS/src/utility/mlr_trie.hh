//
// trie.hh
//
#ifndef MLR_TRIE_HH_
#define MLR_TRIE_HH_

#include <inttypes.h>
#include <stdio.h>
#include <assert.h>
#define USE_MAP 1
#if USE_MAP
#include <map>
#endif

#if TRIE_DEBUG
#define TRIE_ASSERT assert
#else

#ifndef FREEBSD_PLAT
#define TRIE_ASSERT(...)
#else
#define TRIE_ASSERT assert
#endif//FREEBSD_PLAT

#endif//TRIE_DEBUG


#define DA_TRIE_VERSION     "1.0.0"
#define DADL_TRIE_VERSION   "1.0.0"
#define DOUBLE_TRIE_VERSION "1.0.0"

namespace b2bmlr {

typedef int32_t DictStateT;
typedef uint8_t DictKeyT;

class Dict {
  public:
    virtual void init() = 0;
    virtual void clear() = 0;
    virtual bool load(const char* filename);
    virtual bool load(FILE* fp) = 0;
    virtual char* load(char* mem, size_t len) = 0;  // load trie from memory
    virtual bool write(const char* filename);
    virtual bool write(FILE* fp) = 0;
	virtual const char *dictVersion( FILE *fp ) = 0;
	virtual const char *toolsVersion( ) = 0;
    virtual const char* get_err() const = 0;
    virtual ~Dict() {};
  public:
    struct MatchInfo{
        int        pos;
        DictStateT state;
    };
    virtual DictStateT match(const DictKeyT* key, int keyLen) = 0;
    virtual int matchAll(const DictKeyT* key, int keyLen,
                         MatchInfo* matches, int matchLen) = 0;
    virtual DictStateT add(const DictKeyT* key, int keyLen, DictStateT id) = 0;
    virtual char*     stats() = 0;
    virtual void      listAllEntries() = 0;
    virtual void      listAllEntriesUtf8() = 0; // assumes encoding in dictionary as UTF-16
    virtual bool      isConsistent() = 0;
  public:
    enum {
        KEY_MAX = (0x1u << (sizeof(DictKeyT)*8))+2,
        KEY_ENDMARKER = KEY_MAX-1
    };
    enum {
        STATE_NOT_FOUND = 0
    };
};

typedef void(*StateChangeCallBackFunc)(void*, DictStateT, DictStateT);

class DoubleArrayTrie : public Dict {
    friend class DoubleTrie;
  public:
    DoubleArrayTrie();
    virtual ~DoubleArrayTrie();

    virtual void  init();
    virtual void  clear();
    
    //
    // load trie from file
    //
    virtual bool load(FILE* fp);
    
    // load tree from memory
    virtual char* load(char* mem, size_t len); 

    //
    // write trie to file
    //
    virtual bool write(FILE* fp);

	virtual const char *dictVersion( FILE *fp );
	virtual const char *toolsVersion( );
    virtual const char* get_err() const;
    
  protected:
    struct BC{
        DictStateT base;
        DictStateT check;
    };
    
    BC *bc;

    bool bcNeedFree;   // if bc is memory mapped, we should not free it here

    struct Header{
        Header(){
            TrieBase = 1;
            length = 0;
        }
        void init(){
            TrieBase = 1;
            length = 0;
        }
        DictStateT TrieBase;
        int32_t    length;
        char       Magic[32];
    };

    Header _header;

    virtual bool checkMagic();
    virtual void setMagic();
    static const char* theTrieMagic;
    void    setLength(int len) { _header.length = len; }
    
  public:
    inline DictStateT& base(DictStateT s) {
        assert(s >= 0);
        assert(s < getLength());        
        return bc[s].base;
    }
    
    inline DictStateT& check(DictStateT s) {
        assert(s >= 0);
        assert(s < getLength());
        return bc[s].check;
    }

    inline bool Check(DictStateT s, DictStateT b) {
        return ( s > 0 && s < getLength() && bc[s].check == b );
    }
    
    //
    //  for consistency check when building trie
    //
    char      *statsStr;
    DictKeyT   *keyStr;
    int        keyStrPOS;
    int        keyStrMax;
    int       *keyLens;
    int        keysSoFar;
    int        keysMax;
    void initKeyStr();
    void cleanKeyStr();
    void appendKeyStr(const DictKeyT* key, int len);
    char       errStr[512];
    
  public:
    bool isConsistent();
    
    
  public:
    virtual DictStateT match(const DictKeyT* key, int keyLen);
    virtual int matchAll(const DictKeyT* key, int keyLen,
                         MatchInfo* matches, int matchLen);
    virtual DictStateT add(const DictKeyT* key, int keyLen, DictStateT id);
    virtual char*     stats();
    virtual void      listAllEntries();
    virtual void      listAllEntriesUtf8();
    
    int key2idx(int k){
        return k+1;
    }
    int idx2key(int i){
        return i-1;
    }
  public:

    DictStateT getNextState(DictStateT s, int key){
        assert(s > 0);
        assert(key > 0);
        assert(base(s) > 0);
        DictStateT t = base(s) + key2idx(key);
        if (t < getLength() && check(t) == s){
            return t;
        }
        return STATE_NOT_FOUND;
    }
    DictStateT addNewChar(DictStateT s, int k, DictStateT id = -1);


    // find the longest substr in the tire, store the length of the substr in
    // the matchedLen, and return the state of the last match 
    DictStateT matchLongest(const DictKeyT* key, int keyLen, int& matchedLen);


    // match the reversed key, start from a child node and walk backwards
    // when walked to the end of the key is the BaseState, there is a match
    // the function returns ture when there is a match, otherwise returns false
    DictStateT matchReverse(const DictKeyT* key, int keyLen, DictStateT initState, int &matchedLen);

    // add the key into the trie without add the extra KEY_ENDMARKER
    DictStateT addPartial(const DictKeyT* key, int keyLen);

    // add the reversed key into the trie without add the extra KEY_ENDMARKER
    DictStateT addPartialRev(const DictKeyT* key, int keyLen);


    int getKeyRev(DictStateT t){
        assert(t > 0);
        if(t==getBaseState()) return KEY_ENDMARKER;
        assert(check(t) > 0);
        DictStateT s = check(t);
        assert(base(s) > 0);
        assert(t > base(s) && idx2key(t - base(s)) < KEY_MAX);
        int ch = idx2key(t - base(s));
        return ch;
    }
    
    int getEntryRev(DictKeyT* key, DictStateT t){
        int len = 0;
        while(t > 0 && t!=getBaseState()){
            assert(check(t) > 0);
            DictStateT s = check(t);
            assert(base(s) > 0);
            assert(t > base(s) && t - base(s) < KEY_MAX);
            key[len++] = idx2key(t - base(s));
            t = s;
        }
        if (t == getBaseState()) return len;
        else {
            return -1;
        }
    }
    
  public:
    // debug helpers
    void printTrie(DictStateT s);

    void printLink(DictStateT s);

  public:
    // Returns the BaseState
    DictStateT getBaseState() { return _header.TrieBase; }
    // Returns the length of the BC array
    int32_t getLength() { return _header.length; } 
   
    DictStateT nextState(DictStateT s, int ch){ return base(s) + key2idx(ch); }
    DictStateT prevState(DictStateT s){ return check(s); }
    
    // get all valid key base on b
    int getAllKeys(int keys[], DictStateT b){
        int n = 0;
        if(base(b) > 0){
            for(int i = 0; i <= KEY_MAX; i++){
                if ((nextState(b,i) < getLength()) && (check(nextState(b,i)) == b)){
                    keys[n++] = i;
                }
            }
        }
        return n;
    }
    
    void listAll(char *ptr, int len, DictStateT s);
    void listAllUtf8(char *ptr, int len, DictStateT s);
    
    bool isEmpty(DictStateT s){
        TRIE_ASSERT(s > 0);
        TRIE_ASSERT(s < getLength());
        return (check(s) <= 0);
    }
    
    bool isValidState(DictStateT s){
        return ( s > 0 );
    }
    
    bool isFree(DictStateT s){
        if (s >= getLength()){
            extend();
            return true;
        }
        return isEmpty(s);
    }
    
    
    void    relocate(DictStateT s, int b);
    
    DictStateT getNewState(DictStateT s, int ch);
    DictStateT setState(DictStateT s, int ch, DictStateT t);
    
    virtual DictStateT getNewBase(int keys[], int numKeys,
                                  int smallestKey, int largestKey);
    virtual bool extend();
    virtual void getState(DictStateT t){}
    virtual void freeState(DictStateT t){
        check(t) = 0;
        base(t) = 0;
    }

  private:
    StateChangeCallBackFunc stateChangeCallBack;
    void*                   stateChangeCallBackData;
  public:
    void SetStateChangeCallBack(StateChangeCallBackFunc func, void* data){
        stateChangeCallBack = func;
        stateChangeCallBackData = data;
    }
};

class DADLTrie : public DoubleArrayTrie {
    friend class DoubleTrie;
  public:
    DADLTrie(){}
    virtual ~DADLTrie(){clear();}
    virtual void init();
    virtual void clear();
  private:
    virtual void getState(DictStateT t);
    virtual void freeState(DictStateT t);
    virtual bool extend();
    virtual DictStateT getNewBase(int keys[], int numKeys,
                                  int smallestKey, int largestKey);
    bool check_list();
    virtual bool checkMagic();
    virtual void setMagic();
    static const char* theTrieMagic;
};
    
class DoubleTrie : public Dict {
  public:
    DoubleTrie():_sepStates(0),_sepStatesNeedFree(false){init();}
    virtual ~DoubleTrie();
    
  public:
    virtual void init();
    virtual void clear();
    virtual bool load(FILE* fp);
    virtual char* load(char* mem, size_t len); // load Double Trie from memory
    virtual bool write(FILE* fp);
	virtual const char *dictVersion( FILE *fp );
	virtual const char *toolsVersion( );
    virtual const char* get_err() const;
  public:
    virtual DictStateT match(const DictKeyT* key, int keyLen);
    virtual int matchAll(const DictKeyT* key, int keyLen,
                         MatchInfo* matches, int matchLen);
    virtual DictStateT add(const DictKeyT* key, int keyLen, DictStateT id);
    virtual char*     stats();
    virtual void      listAllEntries();
    virtual void      listAllEntriesUtf8(); // assumes encoding in dictionary as UTF-16
    virtual bool      isConsistent(){/* FIXME: */ return true; }
    
  private:
    struct Header{
        Header():sepStatesCap(0){}
        void init(){
            sepStatesCap  = 0;
        }
        char       Magic[32];
        DictStateT sepStatesCap; // capacity(length) of the _sepStates array
    };
    struct SeparateState {
        DictStateT right;   // links the state from the left trie to the right trie
        DictStateT id;      // stores the id of the entry, set by add()
    };

  public:
    bool isSepState(DictStateT s){
        return s < -1;  // sep state starts from the second
    }

    // FIXME:
    // need more effecient way for the linkState manipulation
    // possibly map<DictStateT, map<DictStateT, int> > for back pointing
    // setup linked list for empty spots in _sepStates
    void linkState(DictStateT l, DictStateT r){
        assert(l < 0 && -l < _header.sepStatesCap);
#if USE_MAP
        DictStateT oldR = _sepStates[-l].right;
        if(oldR > 0){
            if(_sepStatesReverseMap[oldR].size() <= 1){
                _sepStatesReverseMap.erase(oldR);
            }
            else{
                _sepStatesReverseMap[oldR].erase(l);
            }
        }
        (_sepStatesReverseMap[r])[l] = 1;
#endif
        _sepStates[-l].right = r;
    }
    void linkStateSetId(DictStateT l, DictStateT id){
        assert(l < 0 && -l < _header.sepStatesCap);
        _sepStates[-l].id = id;
    }
    void unlinkState(DictStateT l);
    void relinkState(DictStateT from, DictStateT to){
#if USE_MAP
        std::map<DictStateT, std::map<DictStateT, int> >::iterator i = _sepStatesReverseMap.find(from);
        if (i != _sepStatesReverseMap.end()){
            for(std::map<DictStateT, int>::iterator ii = (*i).second.begin(); ii != (*i).second.end(); ii++){
                assert(_sepStates[-(*ii).first].right == from);
                _sepStates[-(*ii).first].right = to;
            }
            _sepStatesReverseMap[to] = _sepStatesReverseMap[from];
            _sepStatesReverseMap.erase(from);
        }
#else
        for(int i = 0; i < _header.sepStatesCap; i++){
            if (_sepStates[i].right == from) _sepStates[i].right = to;
        }
#endif
    }
    bool hasInLinks(DictStateT s){
#if USE_MAP
        return (_sepStatesReverseMap.find(s) != _sepStatesReverseMap.end());
#else
        for(int i = 0; i < _header.sepStatesCap; i++){
            if (_sepStates[i].right == s) return true;
        }
        return false;
#endif
    }

    DictStateT linkStateGetId(DictStateT l){
        assert(l < 0 && -l < _header.sepStatesCap);
        return _sepStates[-l].id;
    }
    DictStateT getLinkState(DictStateT l){
        assert(l < 0 && -l < _header.sepStatesCap);
        return _sepStates[-l].right;
    }        
    DictStateT getNewSepState();
  private:
    void listAll(char *ptr, int len, DictStateT s);
    void listAllUtf8(char *ptr, int len, DictStateT s);
  private:
    Header _header;
    DADLTrie _left, _right;
    // array links left to the right
    // length of _sepStates >= _leftMaxState
    SeparateState *_sepStates; 
    bool           _sepStatesNeedFree;
    virtual bool checkMagic();
    virtual void setMagic();
    static const char* theTrieMagic;
    char errStr[512];

#if USE_MAP
    std::map<DictStateT, std::map<DictStateT, int> > _sepStatesReverseMap; // reverse mapping of separate states
#endif
};

} // namespace b2bmlr


#endif // MLR_TRIE_HH_
