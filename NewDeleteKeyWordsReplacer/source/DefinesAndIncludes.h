#ifndef DefinesAndIncludes_H
#define DefinesAndIncludes_H

#include <windows.h>
#include <gl/gl.h>
#include <gl/glu.h>
#include "MemTrace.h"

#define glColor(c) glColor4ub((c >> 24) & 255, (c >> 16) & 255, (c >> 8) & 255, (c) & 255)
#define glColorA(c,a) glColor4ub((c >> 24) & 255, (c >> 16) & 255, (c >> 8) & 255, a)
#define DEG_RAD 0.0174532925194f
#define RAD_DEG 57.295780490443f
#define PI_VAL 3.1415926f

#define writeConsole DefinesAndIncludes::write


//#ifdef _DEBUG
//   #define DEBUG_CLIENTBLOCK   new( _CLIENT_BLOCK, __FILE__, __LINE__)
//#else
//   #define DEBUG_CLIENTBLOCK
//#endif // _DEBUG
//
//#ifdef _DEBUG
//#define _CRTDBG_MAP_ALLOC
//#include <stdlib.h>
//#include <crtdbg.h>
//#endif
//
//#ifdef _DEBUG
//#define new DEBUG_CLIENTBLOCK
//#endif


class DefinesAndIncludes
{
public:
	static void write(const char * pszFormat, ...);
};


#endif
