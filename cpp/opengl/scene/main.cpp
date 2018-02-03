/*
    кнопки Up і Down - рух по вісі Z
    Left і Right - переміщення вздовж ординати
    Page Up і Page Down - рух віссю абсцис
    Space - оберт проти годинникової стрілки
    Esc - вихід з програми
*/


#include <GL/gl.h>
#include <GL/glut.h>
#include <math.h>

#define GLT_PI  3.14159265358979323846

GLint w, h;  // розміри вікна

GLfloat fExtent ;  // XZ розмір сцени [-fExtent..fExtent]
GLfloat pos[]={0,0,0,0};
const GLfloat light_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
const GLfloat light_ambient[]  = { 0.0f, 0.0f, 0.0f, 1.0f };
const GLfloat light_diffuse[]  = { 1.0f, 1.0f, 1.0f, 1.0f };

const GLfloat light_position[] = { 2.0f, 5.0f, 5.0f, 0.0f };
const GLfloat mat_ambient[]    = { 0.7f, 0.7f, 0.7f, 1.0f };
const GLfloat mat_diffuse[]    = { 0.8f, 0.8f, 0.8f, 1.0f };
const GLfloat mat_specular[]   = { 1.0f, 1.0f, 1.0f, 1.0f };
const GLfloat high_shininess[] = { 100.0f };
const GLfloat mat_transparent[]={0.0,1.0,0.0,0.6};


bool  Fly=false;       	// прапорець режиму польоту
float angle_x, angle_y;   // кути повороту камери

float x, y, z;  // XYZ камери
float lx,ly,lz; //XYZ точки огляду
float ex,ey,ez;

//**********************************************************

void DrawGround(void)
{
  GLfloat fStep = 1.0f;
  GLfloat yy = -0.4f;
  GLint iLine;

  glColor3ub(0, 0, 255);
  glTranslated (0, 0, 0);

  glBegin(GL_LINES);

    for(iLine = -fExtent; iLine <= fExtent; iLine += fStep)
    {
      glVertex3f(iLine, yy, fExtent);
      glVertex3f(iLine, yy, -fExtent);

      glVertex3f(fExtent, yy, iLine);
      glVertex3f(-fExtent, yy, iLine);
    }
  glEnd();
}

void Picture(void)
{
//створюємо зображення на сцені

    glColor3ub(255, 255, 0);
    glPushMatrix();
        glTranslated(-10,2.6,-10);
        glutSolidTorus(1,2,20,20);
    glPopMatrix();

    glColor3ub(255, 0, 255);
    glPushMatrix();
        glTranslated(10,2.6,-10);
        glutSolidTorus(1,2,20,20);
    glPopMatrix();

    glColor3ub(0, 255, 255);
    glPushMatrix();
        glTranslated(10,2.6,10);
        glRotated(30,15,3,10);
        glutSolidTorus(1,2,20,20);
    glPopMatrix();

    glColor3ub(0, 255, 0);
    glPushMatrix();
        glMaterialfv(GL_FRONT,GL_DIFFUSE,mat_transparent);
        glEnable(GL_BLEND); //дозвіл на накладання кольорів
        glDepthMask(GL_FALSE); //буфер глибини лише для читання
        glBlendFunc(GL_DST_ALPHA,GL_SRC_COLOR); //задаємо параметри накладання кольорів
        glTranslated(-10,2.6,10);
        glRotated(90,-10,2.6,10);
        glutSolidTorus(1,2,20,20);
        glDepthMask(GL_TRUE); //відновлення дозволів буферу на запис
        glDisable(GL_BLEND); //відключаємо накладання кольорів
    glPopMatrix();

    //туман
    glEnable(GL_FOG);
    glFogf(GL_FOG_START,15.0);
    glFogf(GL_FOG_END,-15.0);
    glFogf(GL_FOG_DENSITY,0.1);

    glColor3ub(0, 0, 255);
}

void RenderSceneSphereWorld()
{
  glPushAttrib(GL_COLOR_BUFFER_BIT | GL_POLYGON_BIT);

  glColor3ub(0, 0, 255);
  glClearColor(1.0f, 1.0f, 1.0f, 1.0f );

  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  glLoadIdentity();

  gluLookAt( x, y, z,
             lx, ly, lz,
             0.0f, 1.0,  0.0f );

    pos[0]=x;
    pos[1]=y;
    pos[2]=z;
    pos[3]=1;
    glLightfv(GL_LIGHT0,GL_POSITION,pos);

  DrawGround();
  Picture();

  glutSwapBuffers();
  glPopAttrib();
}

//**********************************************************
void ChangeSizeSphereWorld(int width, int height)
{
  w=width, h= height;

  if(h == 0) h = 1;

  glViewport(0, 0, w, h);

  GLfloat aspectRatio = (GLfloat)w / (GLfloat)h;

  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();

  gluPerspective(60.0f, aspectRatio, 1.0f, 3.0f*fExtent);

  gluLookAt(x, y, z,
            lx, ly, lz,
            0.0, 1.0, 0.0);

    pos[0]=x;
    pos[1]=y;
    pos[2]=z;
    pos[3]=1;
    glLightfv(GL_LIGHT0,GL_POSITION,pos);

  glMatrixMode(GL_MODELVIEW);
  glLoadIdentity();
}
//**********************************************************
double angle = 0;

void TimerFunc(int value)
{
    if(angle > 2*GLT_PI){
        Fly = false;
        angle = 0;
    }
    if (Fly){
    // зміна параметрів руху камери
    // перерахунок координат камери
        if(angle){
            z=pos[2]=cos(angle)*fExtent;
            x=pos[0]=sin(angle)*fExtent;
        }

    angle+=2*GLT_PI/fExtent;


   glutPostRedisplay();

   glutTimerFunc(100, TimerFunc, 1);
 }
}
//**********************************************************
void Keys(unsigned char Key, int, int)
{
  if (Key == 27) exit(0);
  if (Key == 32) //вмикаємо чи вимикаємо режим польоту
  {
   if (Fly)  Fly=false;
        else  Fly=true;

   if (Fly)  glutTimerFunc(100, TimerFunc, 1);
        else  glutPostRedisplay();
   }
}

void processSpecialKeys(int key, int xx, int yy) {
  switch(key){
    case GLUT_KEY_LEFT:{
            x-=0.1;
            lx-=0.1;
    }
    break;
    case GLUT_KEY_RIGHT:{
            x+=0.1;
            lx+=0.1;
    }
    break;
    case GLUT_KEY_UP:{
            z-=0.1;
            lz-=0.1;
    }
    break;
    case GLUT_KEY_DOWN:{
            z+=0.1;
            lz+=0.1;
    }
    break;
    case GLUT_KEY_PAGE_UP:{
            y+=0.1;
            ly+=0.1;
    }
    break;
    case GLUT_KEY_PAGE_DOWN:{
            y-=0.1;
            ly-=0.1;
    }
    break;
    }

// перерахунок координат камери
    gluLookAt(x, y, z,
            lx, ly, lz,
            0.0, 1.0, 0.0);
// перерахунок лампи
    pos[0]=x;
    pos[1]=y;
    pos[2]=z;
    pos[3]=1;
    glLightfv(GL_LIGHT0,GL_POSITION,pos);

  glutPostRedisplay();
}

//**********************************************************
void SetupRC(void)
{
 glClearColor(0.0F, 0.0F, 0.0F, 1.0F);

//  glEnable(GL_DEPTH_TEST);  // перевірка глибини

  fExtent = 20.0f;  // розмір сцени [-fExtent..fExtent]

// встановлення початкових координат камери та точки огляду
    x=0;y=5;z=15;

    lx=0;ly=0;lz=0;

    ex=0;ey=1;ez=0;

    //початкова позиція лампи
    pos[0]=x;
    pos[1]=y;
    pos[2]=z;
    pos[3]=1;

    //відрізаю задні невидимі грані
    glEnable(GL_CULL_FACE);
    glCullFace(GL_BACK);

    //перевірка глибини
    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LESS);

    //включаю світло і лампу №0
    glEnable(GL_LIGHT0);
    glEnable(GL_NORMALIZE);
    glEnable(GL_COLOR_MATERIAL);
    glEnable(GL_LIGHTING);

    glLightfv(GL_LIGHT0, GL_AMBIENT,  light_ambient); //властивість розсіювання світла
    glLightfv(GL_LIGHT0, GL_DIFFUSE,  light_diffuse); //
    glLightfv(GL_LIGHT0, GL_SPECULAR, light_specular); //відбивання світла
    glLightfv(GL_LIGHT0, GL_POSITION, pos); //позиція лампи

    //матеріали подібні світлу
    glMaterialfv(GL_FRONT, GL_AMBIENT,   mat_ambient);
    glMaterialfv(GL_FRONT, GL_DIFFUSE,   mat_diffuse);
    glMaterialfv(GL_FRONT, GL_SPECULAR,  mat_specular);
    glMaterialfv(GL_FRONT, GL_SHININESS, high_shininess);

}


void idle(){
    glutPostRedisplay();
}
int main(int argc, char* argv[])
{
  glutInit(&argc, argv);
  glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);

  glutInitWindowSize(900, 900);
  glutCreateWindow("Scene");
    SetupRC();
  glutDisplayFunc(RenderSceneSphereWorld);
  glutReshapeFunc(ChangeSizeSphereWorld);
  glutSpecialFunc(processSpecialKeys);
  glutKeyboardFunc(Keys);
  glutIdleFunc(idle);

    glutMainLoop();

    return 0;
}
