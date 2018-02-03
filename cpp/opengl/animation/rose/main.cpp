#include <GL/gl.h>
#include <GL/glut.h>
#include <math.h>
#define PI 3.1415926535897932384626433832795

GLfloat z = 0;
GLfloat windowWidth, windowHeight, radians=.0f;

//малювання багатокутників
void drawPoly()
{
    glClearColor(1, 1, 1, 0);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    double radius = 50;
    int step=12;
    double angle = 2*PI / step;
    GLfloat alpha = 0.2f;
    for(step=12;step>3;step--){
        glBegin(GL_POLYGON);
            glColor4f(0.98039216f, 0.50196078f, 0.44705882f, alpha);
            for(double i = 0; i <=step; i++, angle += 2*PI / step)
                glVertex3f(cos(angle) * radius, sin(angle) * radius, 0);
            radius-=5;
            alpha+=0.05f;
        glEnd();
    }
}

//зміна розмірів екрану
void reshape(int w, int h)
{
    if(h == 0){
        h=1;
    }
   glViewport(0, 0, (GLsizei) w, (GLsizei) h);
   glMatrixMode(GL_PROJECTION);
   glLoadIdentity();
   GLfloat aspectRatio = (GLfloat)w/(GLfloat)h;
   if (w <= h){
        windowWidth = 100;
        windowHeight = 100/aspectRatio;
        glOrtho(-100.0, 100.0, -windowHeight,windowHeight, 1.0, -1.0);
   }
   else{
        windowWidth = 100*aspectRatio;
        windowHeight = 100;
      glOrtho(-windowWidth,windowWidth, -100.0, 100.0, 1.0,-1.0);
   }
   glMatrixMode(GL_MODELVIEW);
   glLoadIdentity();
}

//функція, що передається таймеру
void timer(int value){
    radians+=1.0f;
    glLoadIdentity();
    glRotatef(radians,0,0,z);
    z += 0.3f;
    glutPostRedisplay();
    glutTimerFunc(70, timer, 1);
}

//малювання усієї сцени
void draw(){
    glEnable(GL_BLEND);
    glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    drawPoly();
    glFlush();
    glutSwapBuffers();
}

int main(int argc, char **argv)
{
 glutInit(&argc, argv);
 glutInitWindowPosition(400, 100);
 glutInitWindowSize(500, 500);
 glutCreateWindow("Cycling polygons");
 glutDisplayFunc(draw);
 glutReshapeFunc(reshape);
 glutTimerFunc(70,timer,1);
 glutMainLoop();

 return 0;
}
