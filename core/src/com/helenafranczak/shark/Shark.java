package com.helenafranczak.shark;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;


import java.util.Random;

public class Shark extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;


	//ShapeRenderer shapeRenderer;

	Texture[] fish;
	int flapstate = 0;
	float fishY=0;
	float velocity=0;
	Circle fishCircle;
	int score=0;
	int scoringTrash=0;

	int gamestate=0;

	float gravity = 2;

	Texture topBottle;
	Texture bottomBottle;
	Texture apple;
	Texture fishbones;
	Texture angryfish;
	//private Array<Rectangle> trash;
	//private long lastDropTime;

	//float gap = MathUtils.random(0,800-64);

	float gap= 500;
	float maxTrashOffset;
	Random randomGenerator;
	float trashVelocity =4;
	int numberOfTrash=4;
	float[] trashX = new float [numberOfTrash];
	float [] trashXX = new float [numberOfTrash];
	float[] trashOffset = new float[numberOfTrash];
	float [] trashOffset2 = new float [numberOfTrash];
	float distanceBetweenTrash;

	Rectangle[]topTrash;
	Rectangle[]bottomTrash;
	Rectangle[]topTrash1;
	Rectangle[]bottomTrash1;


//	public static float SCALE_RATIO = 512 / Gdx.graphics.getWidth();
//
//	public static Sprite createScaledSprite(Texture texture) {
//		Sprite sprite = new Sprite(texture);
//		sprite.getTexture().setFilter(Texture.TextureFilter.Linear,
//				Texture.TextureFilter.Linear);
//		sprite.setSize(sprite.getWidth() / SCALE_RATIO,
//				sprite.getHeight() / SCALE_RATIO);
//		return sprite;
//	}
//


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.png");
		//shapeRenderer= new ShapeRenderer();
		fishCircle = new Circle();


		fish = new Texture[2];
		fish[0]=new Texture("fishie1.png");
		fish[1]=new Texture("fishie2.png");
		fishY=Gdx.graphics.getHeight()/2-fish[0].getHeight()/2;

		topBottle = new Texture("bottle.png");
		bottomBottle = new Texture ("bottle2.png");
		apple= new Texture("apple.png");
		//fishbones = new Texture("fishbones.png");
		angryfish = new Texture("fish.png");
		maxTrashOffset = Gdx.graphics.getHeight()/2-gap/2 - 100;
		randomGenerator= new Random();
		distanceBetweenTrash=Gdx.graphics.getWidth()/2;
		topTrash = new Rectangle[numberOfTrash];
		bottomTrash = new Rectangle[numberOfTrash];
		topTrash1 = new Rectangle[numberOfTrash];
		bottomTrash1 = new Rectangle[numberOfTrash];



		for ( int i=0; i<numberOfTrash; i++){ // creating new trash

			trashX[i]=Gdx.graphics.getWidth() - bottomBottle.getWidth() +Gdx.graphics.getWidth() +i* distanceBetweenTrash;
			trashXX[i]=Gdx.graphics.getWidth()/2 - topBottle.getWidth()/2 +Gdx.graphics.getWidth()+i* distanceBetweenTrash;

			//topTrash[i]= new Rectangle();
			bottomTrash[i] = new Rectangle();
			topTrash1[i]= new Rectangle();
			bottomTrash1[i] = new Rectangle();


		}

	}


	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


		if (gamestate != 0) {

			if (trashX[scoringTrash] <Gdx.graphics.getWidth()/2 && trashXX[scoringTrash] < Gdx.graphics.getWidth()/2){

score++;

				Gdx.app.log("Score", String.valueOf(score));

				if(scoringTrash<numberOfTrash-1){

					scoringTrash++;
				}else{

					scoringTrash=0;
				}

			}

			if (Gdx.input.justTouched()) {

				velocity=-30;


			}

			for ( int i=0; i<numberOfTrash; i++) {

				if (trashX[i]<=bottomBottle.getWidth() ){

					trashX[i] += numberOfTrash * distanceBetweenTrash;
					trashXX[i] += numberOfTrash * distanceBetweenTrash;

					trashOffset2[i] = (randomGenerator.nextFloat() -0.5f )* (Gdx.graphics.getHeight());
					trashOffset[i] = (randomGenerator.nextFloat() -0.5f)* (Gdx.graphics.getHeight()-gap-200); // this makes trash move up and donw

				} else {

					trashX[i] = trashX[i] - trashVelocity;
					trashXX[i] = trashXX[i] - trashVelocity;



				}

				//batch.draw(apple, trashX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + trashOffset[i]);
				batch.draw(bottomBottle, trashX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomBottle.getHeight() + trashOffset[i]);
				batch.draw(topBottle, trashXX[i], Gdx.graphics.getHeight() /2 + gap / 2 + trashOffset[i]);
				batch.draw(angryfish, trashXX[i], Gdx.graphics.getHeight() /2 - gap / 2 - angryfish.getHeight() + trashOffset[i]);

				//topTrash[i] =  new Rectangle( trashX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + trashOffset[i],apple.getWidth(), apple.getHeight() );
				bottomTrash [i]= new Rectangle(trashX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomBottle.getHeight() + trashOffset[i], bottomBottle.getWidth(), bottomBottle.getHeight());
				topTrash1 [i]= new Rectangle(trashXX[i], Gdx.graphics.getHeight() /2 + gap / 2 + trashOffset[i], topBottle.getWidth(), topBottle.getHeight());
				bottomTrash1 [i]= new Rectangle(trashXX[i], Gdx.graphics.getHeight() /2 - gap / 2 - angryfish.getHeight() + trashOffset[i], angryfish.getWidth(), angryfish.getHeight());

			}

			if (fishY>0 || velocity<0) {

				velocity = velocity + gravity;
				fishY -= velocity;

			}

		}else{

			if (Gdx.input.justTouched()) {

				gamestate = 1;

			}
		}


		if (flapstate == 0) {
			flapstate = 1;
		} else {

			flapstate = 0;
		}




		batch.draw(fish[flapstate], Gdx.graphics.getWidth() / 2 - fish[flapstate].getWidth() / 2, fishY);
		batch.end();

		fishCircle.set(Gdx.graphics.getWidth()/2, fishY + fish[flapstate].getHeight()/2,fish[flapstate].getWidth()/2 );

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);

		//shapeRenderer.circle(fishCircle.x, fishCircle.y, fishCircle.radius);

		for ( int i=0; i<numberOfTrash; i++) {

		//shapeRenderer.rect(trashX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + trashOffset[i],fishbones.getWidth(), fishbones.getHeight());
			//shapeRenderer.rect(trashX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomBottle.getHeight() + trashOffset[i], bottomBottle.getWidth(), bottomBottle.getHeight());
			//shapeRenderer.rect(trashXX[i], Gdx.graphics.getHeight() /2 + gap / 2 + trashOffset2[i], topBottle.getWidth(), topBottle.getHeight());
					//shapeRenderer.rect(trashXX[i], Gdx.graphics.getHeight() /2 - gap / 2 - angryfish.getHeight() + trashOffset2[i], angryfish.getWidth(), angryfish.getHeight());



			if(Intersector.overlaps(fishCircle, topTrash1[i])|| Intersector.overlaps(fishCircle, bottomTrash[i])||Intersector.overlaps(fishCircle,bottomTrash1[i])){

Gdx.app.log("collision","yes");


			}


		}

		//shapeRenderer.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();

	}
}
