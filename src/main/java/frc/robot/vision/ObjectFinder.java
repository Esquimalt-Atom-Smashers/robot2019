package frc.robot.vision;

import java.util.LinkedList;
import frc.robot.Functions;
import org.opencv.core.Mat;

public class ObjectFinder
{
	//CONSTANTS
	
	private static final float PASS_PERCENT = 0.01f; //percent that need to be true to pass a index ie. 10% need to pass for c to be considered important
	private static final int MIN_GROUP_SIZE = 5;
	
	private int tolerance = 65;
	
	private int targetRed;
	private int targetBlue;
	private int targetGreen;
	/*
	private int thing;
	private int tf;

	public void tfdef() 
	{
		return;	
	}

	public class test implements Functions
	{
		public test()
		{

		}

		public void tdf()
		{
			int t = 1 + 2;
			int b = t++;
			if (t > 0) return;
			if (t < 0) return;
			switch (t)
			{
				case 1:
					return;
				default: return;

			}

		}

		public void sd()
		{
			return;
		}
	}
	*/
	
	Objects targetObject;
	
	PixelGroup [] pixelGroups;
	
	public ObjectFinder(Objects targetObject)
	{
		this.targetObject = targetObject;
		
		switch (targetObject)
		{
			case BALL:
				targetRed = 254;
				targetBlue = 126;
				targetGreen = 101;
				break;
			case TAPE:
				targetRed = 0;
				targetBlue = 0;
				targetGreen = 0;
				break;
		}
	}
	
	public static enum Objects
	{
		BALL,
		TAPE;
	}
	
	public LinkedList<PixelGroup> findObject(Mat image)
	{
		if(image == null) return null;
		long now = System.currentTimeMillis(); //time from start to finish from loading image to end of processing
		LinkedList<PixelGroup> pixelGroups = new LinkedList<PixelGroup>();
		final int imageWidth = image.width();
		final int imageHeight = image.height();
		//int[][][] pixelMap = ImageParser.pixelMap(image); //3d array	[cs of pixels]		[the pixel in the c]	[RGB value Index using ImageParser."the color"]
		//this will print the rgb values of the central pixel, this can help with tuning for different colors or lighting
		
		int[] columnScore = new int[imageHeight];
		boolean[] passedColumns = new boolean [imageHeight];
		int passCount = (int) (PASS_PERCENT * imageHeight); //the number of pixels in a c that needs to have passed (to avoid the odd spec that passes)
		boolean[][] pixels = new boolean[imageHeight/2][imageWidth/2]; //create an boolean array the holds a bool value each for each pixel to store if they passed the tolerence test
		System.out.print("width: ");System.out.println(imageWidth);
		//find pixels that pass the color test
		for (int r = 0; r < image.height(); r += 2)
		{
			for (int c = 0;  c < image.width(); c += 2)
			{
				double[] BGR = image.get(r, c);
				//System.out.println(BGR.length);
				if 
				(
					Math.abs(BGR[ImageParser.RED] - targetRed) <= tolerance &&
					Math.abs(BGR[ImageParser.BLUE] - targetBlue) <= tolerance &&
					Math.abs(BGR[ImageParser.GREEN] - targetGreen) <= tolerance 
				)
				{
					pixels[c][r] = true;
					columnScore[c] ++;
					if (columnScore[c] > passCount) passedColumns[c] = true;
				}
				
			}
		}
		
		int start = 0;
		int end;
		for (int c = 0; c < passedColumns.length; c ++) //this looks for passed cs next to each other 
		{
			if (passedColumns[c])
			{
				start = c; //find a passed c meaning i is the starting point
				while (passedColumns[c]) //until hits a fail c
				{
					c ++;
					if (c >= passedColumns.length) break;
				}
				end = c - 1; //see this above
				if (end - start > MIN_GROUP_SIZE)
				{
					boolean[][] pixelArea = new boolean[end - start][imageWidth];
					
					for (int index = start; index < end; index ++)
					{
						pixelArea[index - start] = pixels[index]; //fill the temp array
					}
					
					pixelGroups.add(new PixelGroup(pixelArea, start));
				}
			}
		}
		System.out.println(("Processing Time: " + Long.toString(System.currentTimeMillis() - now)) + " ms"); //print the processing time
		
		
		return pixelGroups;
	}
	
	public PixelGroup[] rankAndSortGroups(LinkedList<PixelGroup> groups)
	{
		float totalArea = 0;
		int listSize = groups.size();
		PixelGroup[] sortedGroup = new PixelGroup[listSize];
		if (listSize > 1)
		{
			
			for (int i = 0; i < listSize; i ++) //fills array and calculates the total Area of the pixel Groups
			{
				PixelGroup temp = groups.get(i);
				totalArea += temp.getArea();
				sortedGroup[i] = temp;			
			}
			
			for (PixelGroup temp : sortedGroup) //sets the percent of the total area covered by the group
			{
				temp.setAreaPercent(temp.getArea()/totalArea);
			}
			
			boolean sorted = false;
			
			while(!sorted) //sort with bubble sort because it simple, groups are small we can change if we want
			{
				sorted = true;
				for (int i = 0; i < listSize - 1; i ++)
				{
					if (sortedGroup[i].getArea() < sortedGroup[i + 1].getArea()) //sorts from high to low
					{
						PixelGroup temp = sortedGroup[i + 1];
						sortedGroup[i + 1] = sortedGroup[i];
						sortedGroup[i] = temp;
						sorted = false;
					}
				}
			}
			
			
		}
		else
		{
			if(listSize != 0)sortedGroup[0] = groups.get(0);
		}
		for (int i = 0; i < listSize; i ++)
		{
			sortedGroup[i].setRank(i + 1); //so number 1 rank is 1 not 0
		}
		return sortedGroup;
		
	}
	
	
	
	public int[] calabrate(Mat image) //takes average RGB in photo for example put a photo of just the ball.
	{
		int[][][] pixelMap = ImageParser.pixelMap(image);
		int area, red, green, blue, alpha;
		alpha = red = green = blue = 0;
		area = pixelMap.length * pixelMap[0].length; //the area of area
		for (int r = 0; r < pixelMap.length; r ++)
		{
			for (int c = 0; c < pixelMap[r].length; c ++)
			{
				red += pixelMap[r][c][ImageParser.RED];
				blue += pixelMap[r][c][ImageParser.BLUE];
				green += pixelMap[r][c][ImageParser.GREEN];

			}
		}
		
		int[] RGB = new int[4];
		RGB[ImageParser.RED] = red/area;
		RGB[ImageParser.BLUE] = blue/area;
		RGB[ImageParser.GREEN] = green/area;
		
		return RGB;
		
	}
}

