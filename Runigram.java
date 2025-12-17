import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {

	public static void main(String[] args) {
        // קריאת תמונה
        Color[][] ironman = read("ironman.ppm");

        // בדיקה: האם התמונה נקראה?
        if (ironman == null) {
            System.out.println("Error: Could not read image!");
            return;
        }

        // שינוי גודל (למשל: מקטין לחצי רוחב וחצי גובה)
        System.out.println("Scaling image...");
        Color[][] scaledImage = scaled(ironman, 200, 300);

        // --- החלק שפותח חלון ---
        setCanvas(scaledImage); // מגדיר את הגודל לפי התמונה החדשה
        display(scaledImage);   // מצייר
    }

	/** Returns a 2D array of Color values, representing the image data
	 * stored in the given PPM file. */
	public static Color[][] read(String fileName) {
		In in = new In(fileName);
		// Reads the file header, ignoring the first and the third lines.
		in.readString();
		int numCols = in.readInt();
		int numRows = in.readInt();
		in.readInt();
		// Creates the image array
		Color[][] image = new Color[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int r = in.readInt();
                int g = in.readInt();
                int b = in.readInt();
                image[i][j] = new Color(r, g, b);
            }
        }
        return image;
    }
	

    // Prints the RGB values of a given color.
	private static void print(Color c) {
	    System.out.print("(");
		System.out.printf("%3s,", c.getRed());   // Prints the red component
		System.out.printf("%3s,", c.getGreen()); // Prints the green component
        System.out.printf("%3s",  c.getBlue());  // Prints the blue component
        System.out.print(")  ");
	}

	// Prints the pixels of the given image.
	// Each pixel is printed as a triplet of (r,g,b) values.
	// This function is used for debugging purposes.
	// For example, to check that some image processing function works correctly,
	// we can apply the function and then use this function to print the resulting image.
	private static void print(Color[][] image) {
        if (image == null) {return;}
        
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                print(image[i][j]);
            }
            System.out.println(); 
		}
	}

	
	/**
	 * Returns an image which is the horizontally flipped version of the given image. 
	 */
	public static Color[][] flippedHorizontally(Color[][] image) {
		if (image == null) {return null;}
        Color[][] imageFlipped = new Color[image.length][image[0].length];
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                imageFlipped[i][j] = image[i][image[0].length - 1 - j];
            }
        }
        return imageFlipped;
	}
		
	
	
	/**
	 * Returns an image which is the vertically flipped version of the given image. 
	 */
	public static Color[][] flippedVertically(Color[][] image){
		if (image == null) {return null ;}
        Color[][] imageFlipped = new Color[image.length][image[0].length];
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                imageFlipped[i][j] = image[image.length - i -1][j];
            }
        }		return imageFlipped;
	}
	
	// Computes the luminance of the RGB values of the given pixel, using the formula 
	// lum = 0.299 * r + 0.587 * g + 0.114 * b, and returns a Color object consisting
	// the three values r = lum, g = lum, b = lum.
	private static Color luminance(Color pixel) {
		int r = pixel.getRed();
        int g = pixel.getGreen();
        int b = pixel.getBlue();
		int lum = (int) (0.299 * r + 0.587 * g + 0.114 * b);		
		return new Color(lum, lum, lum);
	}
	
	/**
	 * Returns an image which is the grayscaled version of the given image.
	 */
	public static Color[][] grayScaled(Color[][] image) {
			if (image == null) {return null;}
		Color[][] imageGray = new Color[image.length][image[0].length];
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                imageGray[i][j] = luminance( image[i][j]);
	}	
}
	return imageGray;
	}
	/**
	 * Returns an image which is the scaled version of the given image. 
	 * The image is scaled (resized) to have the given width and height.
	 */
	public static Color[][] scaled(Color[][] image, int width, int height) {
		if (image == null) return null;
		int sourceHeight = image.length;
        int sourceWidth = image[0].length;
        Color[][] newImage = new Color[height][width];
		for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
				int sourceRow = i * sourceHeight / height;
				int sourceCol = j * sourceWidth / width;
                newImage[i][j] = image[sourceRow][sourceCol];
            }
        }
        return newImage;
	}
	
	/**
	 * Computes and returns a blended color which is a linear combination of the two given
	 * colors. Each r, g, b, value v in the returned color is calculated using the formula 
	 * v = alpha * v1 + (1 - alpha) * v2, where v1 and v2 are the corresponding r, g, b
	 * values in the two input color.
	 */
	public static Color blend(Color c1, Color c2, double alpha) {
		int r = (int) (alpha * c1.getRed() + (1 - alpha) * c2.getRed());
        int g = (int) (alpha * c1.getGreen() + (1 - alpha) * c2.getGreen());
        int b = (int) (alpha * c1.getBlue() + (1 - alpha) * c2.getBlue());
        
        return new Color(r, g, b);
	}
	
	/**
	 * Cosntructs and returns an image which is the blending of the two given images.
	 * The blended image is the linear combination of (alpha) part of the first image
	 * and (1 - alpha) part the second image.
	 * The two images must have the same dimensions.
	 */
	public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
		int rows = image1.length;
        int cols = image1[0].length;
        Color[][] newImage = new Color[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newImage[i][j] = blend(image1[i][j], image2[i][j], alpha);
            }
        }
        return newImage;
	}

	/**
	 * Morphs the source image into the target image, gradually, in n steps.
	 * Animates the morphing process by displaying the morphed image in each step.
	 * Before starting the process, scales the target image to the dimensions
	 * of the source image.
	 */
	public static void morph(Color[][] source, Color[][] target, int n) {
		if (target.length != source.length || target[0].length != source[0].length) {
            target = scaled(target, source[0].length, source.length);
        }
        
        for (int i = 0; i <= n; i++) {
            double alpha = (double)(n - i) / n;
            
            Color[][] stepImage = blend(source, target, alpha);
            
            display(stepImage);
            StdDraw.pause(500); 
        }	
	}
	
	/** Creates a canvas for the given image. */
	public static void setCanvas(Color[][] image) {
		StdDraw.setTitle("Runigram 2023");
		int height = image.length;
		int width = image[0].length;
		StdDraw.setCanvasSize(height, width);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
        // Enables drawing graphics in memory and showing it on the screen only when
		// the StdDraw.show function is called.
		StdDraw.enableDoubleBuffering();
	}

	/** Displays the given image on the current canvas. */
	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Sets the pen color to the pixel color
				StdDraw.setPenColor( image[i][j].getRed(),
					                 image[i][j].getGreen(),
					                 image[i][j].getBlue() );
				// Draws the pixel as a filled square of size 1
				StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
}

