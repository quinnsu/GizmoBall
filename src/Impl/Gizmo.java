package Impl;

import Config.GizmoShape;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;

import java.awt.*;
import java.util.ArrayList;

public class Gizmo {
    private static World world;
    private static BoardImpl board = new BoardImpl();
    private int x;
    private int y;
    private static Body ground;
    private static BodyType move = BodyType.DYNAMIC;
    private static int rowNum;

    private Body body;
    private GizmoShape gizmoShape;
    private int sizeRate = 1;
    private final static int size = 5;
    private double angle = 0 * Math.PI / 180; //旋转角度
    private Image img;


    public Gizmo(int x, int y, int sizeRate, GizmoShape gizmoShape, Image img) {
        this.x = x;
        this.y = y;
        this.sizeRate = sizeRate;
        this.gizmoShape = gizmoShape;
        this.img = img;
        createBody();
    }

    public Body getBody() {
        return body;
    }

    public static void setWorld(World world) {
        Gizmo.world = world;
        Gizmo.rowNum = board.getRowNum();
        Gizmo.world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body body1 = contact.getFixtureA().getBody();
                Body body2 = contact.getFixtureB().getBody();
                if (body2.getUserData() == GizmoShape.Ball) {
                    Body b = body1;
                    body1 = body2;
                    body2 = b;
                }
                if (body1.getUserData() == GizmoShape.Ball && body2.getUserData() == GizmoShape.Absorber) {
                    body1.setUserData(null);
                    Gizmo.world.destroyBody(body1);
                }

            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold manifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse) {

            }


        });
    }

    public GizmoShape getShape() {
        return gizmoShape;
    }

    public void createBody() {
        switch (gizmoShape) {
            case Ball:
                addBall(x, y);
                break;
            case Absorber:
                addSquare(x, y, sizeRate);
                break;
            case Triangle:
                addTriangle(x, y, sizeRate);
                break;
            case Circle:
                addCircle(x, y, sizeRate);
                break;
            case Square:
                addSquare(x, y, sizeRate);
                break;
            case Paddle:
                addPaddle(x, y, sizeRate);
                break;
            case Track:
                addTrack(x, y);
                break;
            case Curve:
                addCurve(x, y);
                break;
        }
        body.setUserData(gizmoShape);
    }

    public static void setMove(boolean f) {
        if (f)
            move = BodyType.DYNAMIC;
        else
            move = BodyType.STATIC;
    }

    public void updateBody() {
        world.destroyBody(body);
        createBody();
    }

    public static int getLength() {
        return size * rowNum;
    }

    private void addBall(int x, int y) {
        y = 20 - y;
        BodyDef def = new BodyDef();
        def.type = BodyType.DYNAMIC;
        float r = size / 2.0f;
        def.position.set(x * size + r / 2.0f, y * size - r / 2.0f);
        do {
            body = world.createBody(def);
        } while (body == null);
        CircleShape circle = new CircleShape();
        circle.setRadius(r / 2.0f);
        try {
            body.createFixture(circle, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public Image getImg() {
        return img;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSizeRate() {
        return sizeRate;
    }

    public void setSizeRate(int sizeRate) {
        this.sizeRate = sizeRate;
    }

    public static void setRowNum(int rowNum) {
        Gizmo.rowNum = rowNum;
    }

    public void move(int dx, int dy) {
        Vec2 position = body.getPosition();
        body.setTransform(new Vec2(position.x + dx, position.y + dy), 0);
    }

    public static void addSingleBoarder(int x, int y) {
        BodyDef def = new BodyDef();
        def.type = BodyType.STATIC;
        def.position.set(x * size * rowNum, y * size * rowNum);
        Body body = world.createBody(def);
        PolygonShape box = new PolygonShape();
        if (y == x) {
            box.setAsBox(size * rowNum, 0);
        } else {
            box.setAsBox(0, size * rowNum);
        }
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.restitution = 1;
        body.createFixture(fixtureDef);
        if (x == 0 && y == 0)
            ground = body;
    }

    //长方体 黑洞
    public void addSquare(int x, int y, int sizeRate) {
        y = 20 - y;
        BodyDef def = new BodyDef();
        def.type = BodyType.STATIC;
        def.gravityScale = 0;
        def.angularDamping = board.getAngularResistForce();
        def.linearDamping = board.getLinearResistForce();
        int a = sizeRate * size;
        def.position.set(x * size + a / 2.0f, y * size - a / 2.0f);
        def.userData = img;
        body = world.createBody(def);
        PolygonShape box = new PolygonShape();
        box.setAsBox(a / 2.0f, a / 2.0f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 50;
        fixtureDef.restitution = 1;//补偿系数[0..1] 为0时不会回弹
        if (gizmoShape == GizmoShape.Absorber)
            fixtureDef.restitution = 0;//黑洞补偿系数为0
        body.createFixture(fixtureDef);
    }

    //圆形障碍物
    private void addCircle(int x, int y, int sizeRate) {
        y = 20 - y;
        BodyDef def = new BodyDef();
        def.type = BodyType.STATIC;
        def.gravityScale = 0;
        def.angularDamping = board.getAngularResistForce();
        def.linearDamping = board.getLinearResistForce();
        int r = sizeRate * size;
        def.position.set(x * size + r / 2.0f, y * size - r / 2.0f);
        body = world.createBody(def);
        CircleShape circle = new CircleShape();
        circle.setRadius(r / 2.0f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 50;
        fixtureDef.restitution = 1;
        body.createFixture(fixtureDef);
    }

    //三角形障碍物
    public void addTriangle(int x, int y, int sizeRate) {
        y = 20 - y;
        BodyDef def = new BodyDef();
        def.type = BodyType.STATIC;
        def.gravityScale = 0;
        def.angularDamping = board.getAngularResistForce();
        def.linearDamping = board.getLinearResistForce();
        int a = sizeRate * size;
        def.position.set(x * size + a / 2.0f, y * size - a / 2.0f);
        def.angle = (float) -angle;
        body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        float r = a / 2.0f;
        shape.set(new Vec2[]{new Vec2(-r, -r), new Vec2(-r, r), new Vec2(r, -r)}, 3);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 50;
        fixtureDef.restitution = 1;
        body.createFixture(fixtureDef);
    }

    public void addPaddle(int x, int y, int sizeRate) {
        y = 20 - y;
        BodyDef def = new BodyDef();
        def.type = BodyType.STATIC;
        int a = sizeRate * size;
        def.position.set(x * size + a / 2.0f, y * size - size * 0.875f);
        body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f * a, 0.125f * a);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = 1.5f;
        body.createFixture(fixtureDef);
    }

    public void addTrack(int x, int y) {
        y = 20 - y;
        BodyDef def = new BodyDef();
        def.type = BodyType.STATIC;

        def.position.set(x * size + 0.875f * size, y * size - size);
        body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.125f * size, size);
        body.createFixture(shape, 1);
    }

    public void addCurve(int x, int y) {
        y = 20 - y;
        BodyDef def = new BodyDef();
        def.type = BodyType.STATIC;
        def.position.set(x * size + 0.875f * size, y * size - size);
        body = world.createBody(def);
        ChainShape shape = new ChainShape();

        ArrayList<Vec2> groundVertices = new ArrayList<>();

        for (int i = 0; i < size; ++i) {
            Vec2 segment = new Vec2((float) x - 3.5f * size, i);
            groundVertices.add(segment);
        }
        for (int i = 0; i < size; ++i) {
            Vec2 segment = new Vec2((float) i, y - 3.5f * size);
            groundVertices.add(segment);
        }

        Vec2[] spec = {};
        shape.createChain(groundVertices.toArray(spec), groundVertices.size());

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = 1.5f;
        body.createFixture(fixtureDef);
    }

    public void applyForce() {
        body.applyAngularImpulse(750.0f * (getShape() == GizmoShape.Paddle ? 1 : -1));
    }
}
