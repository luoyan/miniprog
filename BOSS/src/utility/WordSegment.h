#ifndef __WORD_SEGMENT__
#define __WORD_SEGMENT__

#include <string>
#include <vector>

namespace ws { class AliWS; }

namespace b2bmlr {

class WordSegment /// 分词
{
public:
	struct WordInfo /// 分词结果：文本和 word id
	{
		WordInfo(const std::string &t, int32_t id, int32_t tag)
				: text(t), wordId(id), tagId(tag) {}
		WordInfo() {}

		std::string text; ///< 词的文本
		int32_t wordId;   ///< 词的 word id
		int32_t tagId;    ///< 词性
	};

	WordSegment() : m_ws(0) {}
	WordSegment(const char *config) : m_ws(0) { init(config); }
	~WordSegment();

	bool init(const char *config);
	const std::vector<WordInfo> segment(const char *text) const;
	const std::vector<std::string> segmentIntoStrings(const char *text) const;
	const std::vector<int32_t> segmentIntoIds(const char *text) const;
	void segment(const char *text, std::vector<WordInfo> &result) const;
	void segment(const char *text, std::vector<std::string> &result) const;
	void segment(const char *text, std::vector<int32_t> &result) const;
	void * getSegmenter() { return m_ws; }

private:
	ws::AliWS *m_ws;
};

} // namespace comrank

#endif // __WORD_SEGMENT__
