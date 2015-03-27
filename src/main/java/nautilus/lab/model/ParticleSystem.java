package nautilus.lab.model;

import java.awt.Image;

import nautilus.lab.graphics.IGraphics;


public class ParticleSystem {
	
	private Particle[] particles;
	private int particleCount = 0;
	private float[] direction;
	private float[] emitter; //source's position of the particle system 
	private Image sprite;
	
	
	public ParticleSystem(){
	}
	
	public void initilize(int numOfParticle){
		particles = new Particle[numOfParticle];
		particleCount = numOfParticle;
		for(int i=0; i<numOfParticle; i++){
			particles[i] = new Particle();
			particles[i].screen = new float[2];
			particles[i].pos = new float[3];
			particles[i].velocity = new float[3];
			
			particles[i].pos[0] = emitter[0];
			particles[i].pos[1] = emitter[1];
			particles[i].pos[2] = emitter[2];
			particles[i].timeLife = 1.0f;
		}
	}
	
	public void draw(IGraphics g){
		int i;
		for(i=0; i<particleCount; i++){
			particles[i].pos[0] += particles[i].velocity[0];
			particles[i].pos[1] += particles[i].velocity[1];
			particles[i].pos[2] += particles[i].velocity[2];
			particles[i].timeLife -= particles[i].fade;
			
			if(particles[i].timeLife <= 0.0f){
				//reset this particle
				particles[i].pos[0] = emitter[0];
				particles[i].pos[1] = emitter[1];
				particles[i].pos[2] = emitter[2];
				particles[i].timeLife = 1.0f;
			}
		}
	}
	
	class Particle{
		float[] screen;
		float[] pos;
		float[] velocity;
		float timeLife = 1.0f;
		float fade = 0;
		int color;
		
		public Particle(){
			//we got a fade value in [0.003 -> 0.103]
			fade = (float)(0.003f + Math.random()/10);
		}
	}
}

