#ifndef _COMM_TOOLS_LOGGER_H_
#define _COMM_TOOLS_LOGGER_H_

#include <stdio.h>
#include <stdarg.h>
#include <string>
#include "stdlib.h"
#include "string.h"
#include "stdint.h"

namespace comm_tools
{
	using std::string;
	
	typedef int LogLevel;
	
	const LogLevel OFF_LOG_LEVEL = 0;
	const LogLevel FATAL_LOG_LEVEL = 1;
	const LogLevel ERROR_LOG_LEVEL = 2;
	const LogLevel WARN_LOG_LEVEL = 3;
	const LogLevel INFO_LOG_LEVEL = 4;
	const LogLevel DEBUG_LOG_LEVEL = 5;
	const LogLevel TRACE_LOG_LEVEL = 6;
	const LogLevel ALL_LOG_LEVEL = 7;
	const LogLevel NOT_SET_LOG_LEVEL = -1;
	
	#define DEFAULT_LOG_LEVEL NOT_SET_LOG_LEVEL
	#define MAX_INFO_LEN 4096
	
	class Logger
	{
	public:
	    static Logger &GetLogger();
	    static void SetRootLogger(Logger &logger);
	    bool Init(const string &name, const string &logfile="",
	                      LogLevel loglevel=DEFAULT_LOG_LEVEL, LogLevel deflevel=DEFAULT_LOG_LEVEL);
	
	    bool IsEnableFor(LogLevel loglevel) const { return loglevel <= _level; };
	    
	    bool IsLogStderr() const { return _log_stderr; };
	    void SetLogStderr(bool d=true) { _log_stderr = d; }
	
	    void SetLogLevel(LogLevel loglevel) { _level = loglevel; };
	    LogLevel GetLogLevel() const { return _level; };
	
	    void SetLogLevelToDefault() { _level = _def_level; };
	
	    LogLevel GetDefaultLevel() const { return _def_level; };
	    void SetDefLogLevel(LogLevel loglevel) { _def_level = loglevel; };
	
	    void SetLogName(const string &logname) { _name = logname; };
	
	    int OpenStream(const string &logfile);
	    FILE *GetStream() const { return _file; };
	
	
	    void Info(const char *fmt, ...);
	    void InfoPos(const char *file, int line, const char *fmt, ...);
	
	    void Warn(const char *fmt, ...);
	    void WarnPos(const char *file, int line, const char *fmt, ...);
	    
	    void Error(const char *fmt, ...);
	    void ErrorPos(const char *file, int line, const char *fmt, ...);
	
	    void Debug(const char *fmt, ...);
	    void DebugPos(const char *file, int line, const char *fmt, ...);
	
	    void Trace(const char *fmt, ...);
	    void TracePos(const char *file, int line, const char *fmt, ...);
	
	protected:
	    Logger();
	    Logger(const string &name, const string &logfile="",
	           LogLevel loglevel=DEFAULT_LOG_LEVEL,
	           LogLevel deflevel=DEFAULT_LOG_LEVEL);// */
	    virtual ~Logger();//bad design! Logger is not a base class, needn't virtual, even virtual, never set the access level protected for deconstructor
	 
	private:
	    int _FormatOutInfo(char * out, const char *type, size_t len);
	    int _FormatOutInfo(char * out, const char *type, size_t len, const char *file, int line);
	    void _Print(const char *outinfo, const char *vabuf);
	    char *_GetTime();
	    void OutPut(const char *type, LogLevel level, va_list ap, const char *fmt);
	    void OutPut(const char *type, LogLevel level, const char *file, int line,
	                va_list ap, const char *fmt);
	
	
	    LogLevel _level;
	    LogLevel _def_level;
	    FILE *_file;
	    string _name;
	    bool _log_stderr;
	    char _time[256];
	
	};
}
#endif //_COMM_TOOLS_LOGGER_H_
