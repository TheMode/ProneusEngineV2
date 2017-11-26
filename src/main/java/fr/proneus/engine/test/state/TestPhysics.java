package fr.proneus.engine.test.state;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.lwjgl.glfw.GLFW;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.shape.Rectangle;
import fr.proneus.engine.state.State;

public class TestPhysics extends State {

	private final World world = new World(new Vec2(0, 1f));

	private Rectangle rectangle;
	private Body body;

	@Override
	public void create(Game game) {
		this.rectangle = new Rectangle(200, 200, 100, 100, Color.RED, true);

		BodyDef boxDef = new BodyDef();
        boxDef.position.set(320 / 30 / 2f, 240 / 30 / 2f);
        boxDef.type = BodyType.DYNAMIC;
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(0.75f, 0.75f);
        Body box = world.createBody(boxDef);
        FixtureDef boxFixture = new FixtureDef();
        boxFixture.density = 0.1f;
        boxFixture.shape = boxShape;
        box.createFixture(boxFixture);
		this.body = box;
		
		BodyDef groundDef = new BodyDef();
        groundDef.position.set(0, game.getVirtualHeight());
        groundDef.type = BodyType.STATIC;
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(1000, 0);
        Body ground = world.createBody(groundDef);
        FixtureDef groundFixture = new FixtureDef();
        groundFixture.density = 1;
        groundFixture.restitution = 0.3f;
        groundFixture.shape = groundShape;
        ground.createFixture(groundFixture);

        BodyDef leftWallDef = new BodyDef();
        leftWallDef.position.set(0, game.getVirtualHeight());
        leftWallDef.type = BodyType.STATIC;
        PolygonShape leftWallShape = new PolygonShape();
        leftWallShape.setAsBox(0, 1000);
        Body leftWall = world.createBody(leftWallDef);
        FixtureDef leftWallFixture = new FixtureDef();
        leftWallFixture.density = 1;
        leftWallFixture.restitution = 0.3f;
        leftWallFixture.shape = leftWallShape;
        leftWall.createFixture(leftWallFixture);

        BodyDef rightWallDef = new BodyDef();
        rightWallDef.position.set(game.getVirtualWidth(), game.getVirtualHeight());
        rightWallDef.type = BodyType.STATIC;
        PolygonShape rightWallShape = new PolygonShape();
        rightWallShape.setAsBox(0, 1000);
        Body rightWall = world.createBody(rightWallDef);
        FixtureDef rightWallFixture = new FixtureDef();
        rightWallFixture.density = 1;
        rightWallFixture.restitution = 0.3f;
        rightWallFixture.shape = rightWallShape;
        rightWall.createFixture(rightWallFixture);

        BodyDef topWallDef = new BodyDef();
        topWallDef.position.set(0, 0);
        topWallDef.type = BodyType.STATIC;
        PolygonShape topWallShape = new PolygonShape();
        topWallShape.setAsBox(1000, 0);
        Body topWall = world.createBody(topWallDef);
        FixtureDef topWallFixture = new FixtureDef();
        topWallFixture.density = 1;
        topWallFixture.restitution = 0.3f;
        topWallFixture.shape = topWallShape;
        topWall.createFixture(topWallFixture);
	}

	@Override
	public void update(Game game) {
		world.step(1 / 60f, 8, 10);
	}

	@Override
	public void render(Game game, Graphics graphic) {
		Vec2 bodyPosition = body.getPosition().mul(30);
		System.out.println(bodyPosition);
		rectangle.x = bodyPosition.x;
		rectangle.y = bodyPosition.y;
		System.out.println(rectangle.x+" "+rectangle.y);
		rectangle.rotate(Math.toDegrees(body.getAngle()));
		graphic.drawShape(rectangle);
	}

	@Override
	public void exit(Game game) {

	}
	
	@Override
	public void onKeyDown(Game game, int key, int scancode) {
		if(key == GLFW.GLFW_KEY_A){
			body.applyAngularImpulse(-0.01f);
		}
	}

	@Override
	public void onMouseMove(Game game, int x, int y) {
		if (game.getInput().isMouseDown(0)) {
			Vec2 mousePosition = new Vec2(x, y).mul(0.5f).mul(1 / 30f);
			Vec2 bodyPosition = body.getPosition();
			Vec2 force = mousePosition.sub(bodyPosition);
			body.applyForce(force, body.getPosition());
		}
	}

}
