#include <GL/gl.h>
#include <GL/glut.h>
#include <math.h>
#define PI 3.1415926535897932384626433832795

GLfloat z = 0;
GLfloat windowWidth, windowHeight, radians=.0f;

void drawPoly()
{
    glClearColor(1, 1, 1, 0);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    double radius = 50;
    int step = 12;
    double angle = 2*PI / step;
    GLfloat alpha = 0.2f;
    glBegin(GL_POLYGON);
        glColor4f(0.98039216f, 0.50196078f, 0.44705882f, alpha);
        for(double i = 0; i <=step; i++, angle += 2*PI / step) //step=12; radius=50
 			glVertex3f(cos(angle) * radius, sin(angle) * radius, 0);
        radius-=5;
        step--;
        alpha+=0.05f;
    glEnd();
    glBegin(GL_POLYGON);
        glColor4f(0.98039216f, 0.50196078f, 0.44705882f, alpha);
        for(double i = 0; i <=step; i++, angle += 2*PI / step) //step=11; radius=50
 			glVertex3f(cos(angle) * radius, sin(angle) * radius, 0);
        radius-=5;
        step--;
        alpha+=0.05f;
    glEnd();
    glBegin(GL_POLYGON);
        glColor4f(0.98039216f, 0.50196078f, 0.44705882f, alpha);
        for(double i = 0; i <=step; i++, angle += 2*PI / step) //step=10; radius=50
 			glVertex3f(cos(angle) * radius, sin(angle) * radius, 0);
        radius-=5;
        step--;
        alpha+=0.05f;
    glEnd();
    glBegin(GL_POLYGON);
        glColor4f(0.98039216f, 0.50196078f, 0.44705882f, alpha);
        for(double i = 0; i <=step; i++, angle += 2*PI / step) //step=9; radius=45
 			glVertex3f(cos(angle) * radius, sin(angle) * radius, 0);
        radius-=5;
        step--;
        alpha+=0.05f;
    glEnd();
    glBegin(GL_POLYGON);
        glColor4f(0.98039216f, 0.50196078f, 0.44705882f, alpha);
        for(double i = 0; i <=step; i++, angle += 2*PI / step) //step=8 radius=40
 			glVertex3f(cos(angle) * radius, sin(angle) * radius, 0);
        radius-=5;
        step--;
        alpha+=0.05f;
    glEnd();
    glBegin(GL_POLYGON);
        glColor4f(0.98039216f, 0.50196078f, 0.44705882f, alpha);
        for(double i = 0; i <=step; i++, angle += 2*PI / step) //step=7; radius=35
 			glVertex3f(cos(angle) * radius, sin(angle) * radius, 0);
        radius-=5;
        step--;
        alpha+=0.05f;
    glEnd();
    glBegin(GL_POLYGON);
        glColor4f(0.98039216f, 0.50196078f, 0.44705882f, alpha);
        for(double i = 0; i <=step; i++, angle += 2*PI / step) //step=6; radius=30
 			glVertex3f(cos(angle) * radius, sin(angle) * radius, 0);
        radius-=5;
        step--;
        alpha+=0.05f;
    glEnd();
    glBegin(GL_POLYGON);
        glColor4f(0.98039216f, 0.50196078f, 0.44705882f, alpha);
        for(double i = 0; i <=step; i++, angle += 2*PI / step) //step=5; radius=25
 			glVertex3f(cos(angle) * radius, sin(angle) * radius, 0);
        radius-=5;
        step--;
        alpha+=0.05f;
    glEnd();
    glBegin(GL_POLYGON);
        glColor4f(0.98039216f, 0.50196078f, 0.44705882f, alpha);
        for(double i = 0; i <=step; i++, angle += 2*PI / step) //step=4; radius=20
 			glVertex3f(cos(angle) * radius, sin(angle) * radius, 0);
    glEnd();

}

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

void timer(int value){
    radians+=1.0f;
    glLoadIdentity();
    glRotatef(radians,0,0,z);
    z += 0.3f;
    glutPostRedisplay();
    glutTimerFunc(70, timer, 1);
}

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
