package com.github.sarxos.webcam.copy;

import java.awt.image.BufferedImage;


public interface WebcamImageTransformer {

	BufferedImage transform(BufferedImage image);

}
