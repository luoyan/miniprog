#include "Logger.h"
#include <time.h>
#include <iostream>

namespace comm_tools
{
	using namespace std;
	Logger &Logger::GetLogger()
	{
	    static Logger logger;
	    return logger;
	}
	
	Logger::Logger()
	:_level(DEFAULT_LOG_LEVEL),
	 _def_level(DEFAULT_LOG_LEVEL),   
	 _file(NULL),
	 _name(""),   
	 _log_stderr(true)   
	{
	}
	
	Logger::Logger(const string &name, const string &logfile,
	           LogLevel loglevel, LogLevel deflevel)
	:_level(loglevel),
	 _def_level(deflevel),
	 _file(NULL),
	 _name(name),   
	 _log_stderr(true) 
	{
	    if(!(logfile.empty()))
	    {
	        _file = fopen(logfile.c_str(), "a+");
	        if (!_file)
	        {
	            cerr << "open log file failed: " << logfile <<endl;
	        }
	    }
	}// */
	
	Logger::~Logger()
	{
	    if (_file != NULL)
	    {
	        fclose(_file);
	    }
	}
	
	bool Logger::Init(const string &name, const string &logfile,
	                  LogLevel loglevel, LogLevel deflevel)
	{
	    _level = loglevel;
	    _def_level = deflevel;
	    _name = name;    
	    if(!(logfile.empty()))
	    {
	        _file = fopen(logfile.c_str(), "a+");
	        if (!_file)
	        {
	            cerr << "open log file failed: " << logfile <<endl;
	            return false;
	        }
	    }
	    return true;
	}
	
	int Logger::OpenStream(const string &logfile)
	{
	    if (!(logfile.empty()))
	    {
	        FILE *fp = fopen(logfile.c_str(), "a+");
	        if (fp != NULL)
	        {
	            FILE *tmp = _file;
	            _file = fp;
	            if (_file != NULL)
	            {
	                fclose(tmp);
	            }
	        }
	        else
	        {
	            return -1;
	        }
	    }
	    return 0;
	}
	
	int Logger::_FormatOutInfo(char *out, const char *type, size_t len)
	{
	    if(out == NULL || type == NULL)
	    {
	        return -1;
	    }
	    snprintf(out, len, "[%s-%s]", _name.c_str(), type);
	    return 0;
	}
	
	int Logger::_FormatOutInfo(char *out, const char *type, size_t len,
	                          const char *file, int line)
	{
	    if(out == NULL || type == NULL)
	    {
	        return -1;
	    }
	    snprintf(out, len, "[%s-%s] %s:%d", _name.c_str(), type, file, line);
	    return 0;
	}
	
	void Logger::_Print(const char *outInfo, const char *vabuf) 
	{
	    if (_file != NULL)
	    {
	        fprintf(_file, "[%s] %s: ", _GetTime(), outInfo);
	        fprintf(_file, "%s", vabuf);
	        fflush(_file);
	    }
	    else
	    {
	#if defined (_DEBUG) || defined (DEBUG)        
	        fprintf(stdout, "[%s] %s: ", _GetTime(), outInfo);
	        fprintf(stdout, "%s\n", vabuf);
	        fflush(stdout);
	#else
	        fprintf(stderr, "[%s] %s: ", _GetTime(), outInfo);
	        fprintf(stderr, "%s\n", vabuf);
	        fflush(stderr);
	#endif        
	    }
	
	}
	
	
	void Logger::Info(const char *fmt, ...)
	{
	    if (!IsEnableFor(INFO_LOG_LEVEL))
	        return;
	    va_list ap;
	    va_start(ap, fmt);
	    OutPut("INFO", INFO_LOG_LEVEL, ap, fmt);
	    va_end(ap);
	}
	
	
	void Logger::InfoPos(const char *file, int line, const char *fmt, ...)
	{
	    if (!IsEnableFor(INFO_LOG_LEVEL))
	        return;
	    va_list ap;
	    va_start(ap, fmt);
	    OutPut("INFO",INFO_LOG_LEVEL, file, line, ap, fmt);
	    va_end(ap);
	}
	
	void Logger::Error(const char *fmt, ...)
	{
	    if (!IsEnableFor(ERROR_LOG_LEVEL))
	        return;
	    va_list ap;
	    va_start(ap, fmt);
	    OutPut("ERROR", ERROR_LOG_LEVEL, ap, fmt);
	    va_end(ap);
	}
	
	void Logger::ErrorPos(const char *file, int line, const char *fmt, ...)
	{
	    if (!IsEnableFor(ERROR_LOG_LEVEL))
	        return;
	    va_list ap;
	    va_start(ap, fmt);
	    OutPut("ERROR", ERROR_LOG_LEVEL, file, line, ap, fmt);
	    va_end(ap);
	}
	
	void Logger::Debug(const char *fmt, ...)
	{
	    if (!IsEnableFor(DEBUG_LOG_LEVEL))
	        return;
	    va_list ap;
	    va_start(ap, fmt);
	    OutPut("DEBUG", DEBUG_LOG_LEVEL, ap, fmt);
	    va_end(ap);
	}
	
	void Logger::DebugPos(const char *file, int line, const char *fmt, ...)
	{
	    if (!IsEnableFor(DEBUG_LOG_LEVEL))
	        return;
	    va_list ap;
	    va_start(ap, fmt);
	    OutPut("DEBUG", DEBUG_LOG_LEVEL, file, line, ap, fmt);
	    va_end(ap); 
	}
	
	void Logger::Trace(const char *fmt, ...)
	{
	    if (!IsEnableFor(TRACE_LOG_LEVEL))
	        return;
	    va_list ap;
	    va_start(ap, fmt);
	    OutPut("TRACE", TRACE_LOG_LEVEL, ap, fmt);
	    va_end(ap);
	
	}
	
	void Logger::TracePos(const char *file, int line, const char *fmt, ...)
	{
	    if (!IsEnableFor(TRACE_LOG_LEVEL))
	        return;
	    va_list ap;
	    va_start(ap, fmt);
	    OutPut("TRACE", TRACE_LOG_LEVEL, file, line, ap, fmt);
	    va_end(ap);
	}
	
	void Logger::Warn(const char *fmt, ...)
	{
	    if (!IsEnableFor(WARN_LOG_LEVEL))
	        return;
	    va_list ap;
	    va_start(ap, fmt);
	    OutPut("WARN", WARN_LOG_LEVEL, ap, fmt);
	    va_end(ap);
	
	}
	
	void Logger::WarnPos(const char *file, int line, const char *fmt, ...)
	{
	    if (!IsEnableFor(WARN_LOG_LEVEL))
	        return;
	    va_list ap;
	    va_start(ap, fmt);
	    OutPut("WARN", WARN_LOG_LEVEL, file, line, ap, fmt);
	    va_end(ap);
	}
	
	
	char *Logger::_GetTime()
	{
	    time_t now;
	    struct tm *timenow;
	    time(&now);
	    timenow = localtime(&now);
	    snprintf(_time, sizeof(_time), "%s", asctime(timenow));
	    _time[strlen(_time)-1] = '\0';
	    return _time;
	}
	
	void Logger::OutPut(const char *type, LogLevel level, va_list ap, const char *fmt)
	{
	    char vabuf[MAX_INFO_LEN];
	    vsnprintf(vabuf, MAX_INFO_LEN, fmt, ap);
	    char buf[MAX_INFO_LEN];
	    _FormatOutInfo(buf, type,MAX_INFO_LEN);
	
	    _Print(buf, vabuf);
	}
	
	void Logger::OutPut(const char *type, LogLevel level, const char *file, int line, 
	                    va_list ap, const char *fmt)
	{
	    char vabuf[MAX_INFO_LEN];
	    vsnprintf(vabuf, MAX_INFO_LEN, fmt, ap);
	    
	    char buf[MAX_INFO_LEN];
	    _FormatOutInfo(buf, type,MAX_INFO_LEN, file, line);
	
	    _Print(buf, vabuf);
	}
}
