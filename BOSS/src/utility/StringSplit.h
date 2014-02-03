#ifndef _COMM_TOOLS_STRING_SPLIT_H_
#define _COMM_TOOLS_STRING_SPLIT_H_

#include <cassert>
#include <cstring>
#include <iostream>
//namespace comm_tools
namespace b2bmlr
{
	class StringSplit
	{
		public:
			StringSplit(const char *srcStr, const char *sp, bool skipEmpty = false)
				:m_buf(NULL), m_segment(NULL), m_size(0)
			{
				_Split(srcStr, sp, skipEmpty);
			}

			~StringSplit()
			{
				delete [] m_buf;
				m_buf = NULL;

				delete [] m_segment;
				m_segment = NULL;

				m_size = 0;
			}
			
			const char * operator[](int i) const
			{
				if ((i >= 0) && (i < m_size))
				{
					return m_segment[i];
				}
				else
				{
					return NULL;
				}
			}
			
			int Size() const
			{
				return m_size;
			}

		private:
			void _Split(const char *srcStr, const char *sp, bool skipEmpty)
			{
				assert(srcStr != NULL);
				assert(sp != NULL);
				
				int srcStrLen = strlen(srcStr);
				m_buf = new char[srcStrLen + 1];
				memcpy(m_buf, srcStr, srcStrLen);
				m_buf[srcStrLen] = 0;
				
				m_segment = new char *[srcStrLen + 1];
				memset(m_segment, 0, sizeof(char *) * (srcStrLen + 1));
				
				if (srcStrLen == 0)
				{
					m_size = 0;
					return;
				}

				int spLen = strlen(sp);
				if (spLen == 0)
				{
					m_segment[0] = m_buf;
					m_size = 1;
					return;
				}
				
				if (!skipEmpty)
				{
					m_segment[m_size++] = m_buf;
					for (int i = 0; i < srcStrLen; i++)
					{
						for (int j = 0; j < spLen; j++)
						{
							if (m_buf[i] == sp[j])
							{
								m_buf[i] = 0;
								m_segment[m_size++] = m_buf + i + 1; 	
								break;
							}
						}
					}

					return;
				}
				
				char *beginPos = m_buf;
				char *endPos = m_buf + srcStrLen - 1;//front code assure srcStrLen > 0;see line 60
				
				while (*beginPos != '\0')
				{
					bool needBreak = true;
					for (int j = 0; j < spLen; j++)
					{
						if (*beginPos == sp[j])
						{
							needBreak = false;	
							break;
						}
					}

					if (needBreak)
					{
						break;
					}

					beginPos++;
				}
				
				while (endPos > m_buf)
				{
					bool needBreak = true;
					for (int j = 0; j < spLen; j++)
					{
						if (*endPos == sp[j])
						{
							needBreak = false;
							break;
						}
					}

					if (needBreak)
					{
						break;
					}

					endPos--;
				}

				if (beginPos > endPos)
				{
					m_size = 0;
					return;
				}

				m_segment[m_size++] = beginPos;
				bool frontSp = false;//as beginPos has check before so false first
				char *cur = beginPos + 1;
				for (; cur < endPos; cur++)
				{
					bool curSp = false;
					for (int j = 0; j < spLen; j++)
					{
						if (*cur == sp[j])
						{
							*cur = 0;
							curSp = true;
						}
					}

					if (frontSp && (!curSp))
					{
						m_segment[m_size++] = cur;
					}

					if (curSp)
					{
						frontSp = true;
					}
					else
					{
						frontSp = false;
					}
				}
				
				if (frontSp)
				{
					m_segment[m_size++] = endPos;
				}

				*(endPos + 1) = '\0';

				return;
			}
			
		private:
			char *m_buf;     //buffer to save srcStr
			char **m_segment;//dynamic array of char * to save every segment
			int m_size;      //number of segments
	};
}
#endif // _COMM_TOOLS_STRING_SPLIT_H_
