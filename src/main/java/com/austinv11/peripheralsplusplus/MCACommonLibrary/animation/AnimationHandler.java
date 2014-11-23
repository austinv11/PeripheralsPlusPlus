package com.austinv11.peripheralsplusplus.MCACommonLibrary.animation;

import com.austinv11.peripheralsplusplus.MCACommonLibrary.IMCAnimatedEntity;
import com.austinv11.peripheralsplusplus.MCACommonLibrary.math.Quaternion;
import com.austinv11.peripheralsplusplus.MCACommonLibrary.math.Vector3f;
import com.austinv11.peripheralsplusplus.client.MCAClientLibrary.MCAModelRenderer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class AnimationHandler {
	/** Owner of this handler. */
	IMCAnimatedEntity animatedEntity;
	/** List of all the activate animations of this Entity. */
	public ArrayList<Channel> animCurrentChannels = new ArrayList();
	/** Previous time of every active animation. */
	public HashMap<String, Long> animPrevTime = new HashMap<String, Long>();
	/** Current frame of every active animation. */
	public HashMap<String, Float> animCurrentFrame = new HashMap<String, Float>();
	/** Contains the unique names of the events that have been already fired during each animation.
	 * It becomes empty at the end of every animation. */
	protected HashMap<String, ArrayList<String>> animationEvents = new HashMap<String, ArrayList<String>>();

	public AnimationHandler(IMCAnimatedEntity entity) {
		animatedEntity = entity;
	}

	public void activateAnimation(HashMap<String, Channel> animChannels, String name, float startingFrame) {
		if(animChannels.get(name) != null)
		{
			Channel selectedChannel = animChannels.get(name);
			int indexToRemove = animCurrentChannels.indexOf(selectedChannel);
			if(indexToRemove != -1)
			{
				animCurrentChannels.remove(indexToRemove);
			}

			animCurrentChannels.add(selectedChannel);
			animPrevTime.put(name, System.nanoTime());
			animCurrentFrame.put(name, startingFrame);
			if(animationEvents.get(name) == null){
				animationEvents.put(name, new ArrayList<String>());
			}
		} else {
			System.out.println("The animation called "+name+" doesn't exist!");
		}
	}

	public abstract void activateAnimation(String name, float startingFrame);

	public void stopAnimation(HashMap<String, Channel> animChannels, String name) {
		Channel selectedChannel = animChannels.get(name);
		if(selectedChannel != null)
		{
			int indexToRemove = animCurrentChannels.indexOf(selectedChannel);
			if(indexToRemove != -1)
			{
				animCurrentChannels.remove(indexToRemove);
				animPrevTime.remove(name);
				animCurrentFrame.remove(name);
				animationEvents.get(name).clear();
			}
		} else {
			System.out.println("The animation called "+name+" doesn't exist!");
		}
	}

	public abstract void stopAnimation(String name);

	public void animationsUpdate() {
		for(Iterator<Channel> it = animCurrentChannels.iterator(); it.hasNext();)
		{
			Channel anim = it.next();
			boolean animStatus = updateAnimation(animatedEntity, anim, animPrevTime, animCurrentFrame);
			if(animCurrentFrame.get(anim.name) != null)
			{
				fireAnimationEvent(anim, animCurrentFrame.get(anim.name));
			}
			if(!animStatus)
			{
				it.remove();
				animPrevTime.remove(anim.name);
				animCurrentFrame.remove(anim.name);
				animationEvents.get(anim.name).clear();
			}
		}
	}

	public boolean isAnimationActive(String name) {
		boolean animAlreadyUsed = false;
		for(Channel anim : animatedEntity.getAnimationHandler().animCurrentChannels)
		{
			if(anim.name.equals(name))
			{
				animAlreadyUsed = true;
				break;
			}
		}
		
		return animAlreadyUsed;
	}

	private void fireAnimationEvent(Channel anim, Float frame)
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			fireAnimationEventClientSide(anim, frame);
		} else {
			fireAnimationEventServerSide(anim, frame);
		}
	}

	@SideOnly(Side.CLIENT)
	public abstract void fireAnimationEventClientSide(Channel anim, Float frame);

	public abstract void fireAnimationEventServerSide(Channel anim, Float frame);

	/** Update animation values. Return false if the animation should stop. */
	public static boolean updateAnimation(IMCAnimatedEntity entity, Channel channel, HashMap<String, Long> prevTimeAnim, HashMap<String, Float> prevFrameAnim)
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isServer() || (FMLCommonHandler.instance().getEffectiveSide().isClient() && !isGamePaused()))
		{
			long prevTime = prevTimeAnim.get(channel.name);
			float prevFrame = prevFrameAnim.get(channel.name);

			long currentTime = System.nanoTime();
			double deltaTime = (currentTime-prevTime) / 1000000000.0;
			float numberOfSkippedFrames = (float) (deltaTime * channel.fps);

			float currentFrame = prevFrame + numberOfSkippedFrames;

			if(currentFrame < channel.totalFrames)
			{
				prevTimeAnim.put(channel.name, currentTime);
				prevFrameAnim.put(channel.name, currentFrame);
				return true;
			} else {
				if(channel.mode == Channel.LOOP)
				{
					prevTimeAnim.put(channel.name, currentTime);
					prevFrameAnim.put(channel.name, 0F);
					return true;
				}
				return false;
			}
		} else {
			long currentTime = System.nanoTime();
			prevTimeAnim.put(channel.name, currentTime);
			return true;
		}
	}

	@SideOnly(Side.CLIENT)
	private static boolean isGamePaused() {
		net.minecraft.client.Minecraft MC = net.minecraft.client.Minecraft.getMinecraft();
		return MC.isSingleplayer() && MC.currentScreen != null && MC.currentScreen.doesGuiPauseGame() && !MC.getIntegratedServer().getPublic();
	}
	
	/** Apply animations if running or apply initial values.
	 * Must be called only by the model class.
	 */
	@SideOnly(Side.CLIENT)
	public static void performAnimationInModel(HashMap<String, MCAModelRenderer> parts, IMCAnimatedEntity entity)
	{
		for (Map.Entry<String, MCAModelRenderer> entry : parts.entrySet()) {
			String boxName = entry.getKey();
			MCAModelRenderer box = entry.getValue();

			boolean anyRotationApplied = false;
			boolean anyTranslationApplied = false;

			for(Channel channel : entity.getAnimationHandler().animCurrentChannels)
			{
				float currentFrame = entity.getAnimationHandler().animCurrentFrame.get(channel.name);

				//Rotations
				KeyFrame prevRotationKeyFrame = channel.getPreviousRotationKeyFrameForBox(boxName, entity.getAnimationHandler().animCurrentFrame.get(channel.name));
				int prevRotationKeyFramePosition = prevRotationKeyFrame != null ? channel.getKeyFramePosition(prevRotationKeyFrame) : -1;

				KeyFrame nextRotationKeyFrame = channel.getNextRotationKeyFrameForBox(boxName, entity.getAnimationHandler().animCurrentFrame.get(channel.name));
				int nextRotationKeyFramePosition = nextRotationKeyFrame != null ? channel.getKeyFramePosition(nextRotationKeyFrame) : 0;

				float SLERPProgress = (currentFrame - (float)prevRotationKeyFramePosition) / ((float)(nextRotationKeyFramePosition - prevRotationKeyFramePosition));
				if(SLERPProgress > 1F || SLERPProgress < 0F)
				{
					SLERPProgress = 1F;
				}

				if(!(nextRotationKeyFramePosition == 0)){
					if(prevRotationKeyFramePosition == -1)
					{
						Quaternion currentQuat = new Quaternion();
						currentQuat.interpolate(parts.get(boxName).defaultRotationAsQuaternion, nextRotationKeyFrame.modelRenderersRotations.get(boxName), SLERPProgress);
						box.rotationMatrix.set(currentQuat).transpose();

						anyRotationApplied = true;
					} else {
						Quaternion currentQuat = new Quaternion();
						currentQuat.interpolate(prevRotationKeyFrame.modelRenderersRotations.get(boxName), nextRotationKeyFrame.modelRenderersRotations.get(boxName), SLERPProgress);
						box.rotationMatrix.set(currentQuat).transpose();

						anyRotationApplied = true;
					}
				}


				//Translations
				KeyFrame prevTranslationKeyFrame = channel.getPreviousTranslationKeyFrameForBox(boxName, entity.getAnimationHandler().animCurrentFrame.get(channel.name));
				int prevTranslationsKeyFramePosition = prevTranslationKeyFrame != null ? channel.getKeyFramePosition(prevTranslationKeyFrame) : -1;

				KeyFrame nextTranslationKeyFrame = channel.getNextTranslationKeyFrameForBox(boxName, entity.getAnimationHandler().animCurrentFrame.get(channel.name));
				int nextTranslationsKeyFramePosition = nextTranslationKeyFrame != null ? channel.getKeyFramePosition(nextTranslationKeyFrame) : 0;

				float LERPProgress = (currentFrame - (float)prevTranslationsKeyFramePosition) / ((float)(nextTranslationsKeyFramePosition - prevTranslationsKeyFramePosition));
				if(LERPProgress > 1F)
				{
					LERPProgress = 1F;
				}

				if(!(nextTranslationsKeyFramePosition == 0)) {
					if(prevTranslationsKeyFramePosition == -1)
					{
						Vector3f startPosition = parts.get(boxName).getPositionAsVector();
						Vector3f endPosition = nextTranslationKeyFrame.modelRenderersTranslations.get(boxName);
						Vector3f currentPosition = new Vector3f(startPosition);
						currentPosition.interpolate(endPosition, LERPProgress);
						box.setRotationPoint(currentPosition.x, currentPosition.y, currentPosition.z);

						anyTranslationApplied = true;
					} else {
						Vector3f startPosition = prevTranslationKeyFrame.modelRenderersTranslations.get(boxName);
						Vector3f endPosition = nextTranslationKeyFrame.modelRenderersTranslations.get(boxName);
						Vector3f currentPosition = new Vector3f(startPosition);
						currentPosition.interpolate(endPosition, LERPProgress);
						box.setRotationPoint(currentPosition.x, currentPosition.y, currentPosition.z);

						anyTranslationApplied = true;
					}
				}
			}

			//Set the initial values for each box if necessary
			if(!anyRotationApplied)
			{
				box.setRotationMatrix(box.defaultRotationMatrix);
			}
			if(!anyTranslationApplied)
			{
				box.setRotationPoint(box.defaultRotationPointX, box.defaultRotationPointY, box.defaultRotationPointZ);				
			}
		}
	}
}
