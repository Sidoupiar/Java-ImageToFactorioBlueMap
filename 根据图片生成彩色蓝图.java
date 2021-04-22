import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.zip.Deflater;

import javax.imageio.ImageIO;

public final class 根据图片生成彩色蓝图
{
	public static final void main( String[] args ) throws IOException
	{
		// 图片路径
		String imagePath = "C:/Users/82763/Desktop/你的图片.png";
		// 蓝图文件路径
		String outputPath = "C:/Users/82763/Desktop/图片蓝图-输出.txt";
		
		
		
		// 基础参数
		String head = "{\"blueprint\":{\"icons\":[{\"signal\":{\"type\":\"item\",\"name\":\"sicw-item-像素块母版\"},\"index\":1}],\"entities\":[";
		String foot = "],\"item\":\"blueprint\",\"version\":"+System.currentTimeMillis()+"}}";
		
		// 创建文件流
		BufferedImage br = ImageIO.read( new File( imagePath ) );
		BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( outputPath ) , "utf-8" ) );
		double lx = ( double ) br.getWidth() / 2D + 0.5D;
		double ly = ( double ) br.getHeight() / 2D + 0.5D;
		
		// 生成蓝图基础代码
		StringBuffer source = new StringBuffer( head );
		int count = 0;
		Color color;
		int r , g , b;
		for( int x = 0 , c = br.getWidth() ; x < c ; x ++ )
		{
			for( int y = 0 , d = br.getHeight() ; y < d ; y ++ )
			{
				count ++;
				color = new Color( br.getRGB( x , y ) );
				r = ( int ) Math.floor( (double)color.getRed()/32D+0.5D ) * 32;
				g = ( int ) Math.floor( (double)color.getGreen()/32D+0.5D ) * 32;
				b = ( int ) Math.floor( (double)color.getBlue()/32D+0.5D ) * 32;
				source.append( "{\"entity_number\":"+count+",\"name\":\"sicw-simple-像素块-"+r+"-"+g+"-"+b+"\",\"position\":{\"x\":"+((double)x-lx)+",\"y\":"+((double)y-ly)+"},\"variation\":1}" );
				if( x < c-1 || y < d-1 ) source.append( "," );
			}
		}
		source.append( foot );
		//System.out.println( "生成结果 : "+source );
		System.out.println( "生成结果长度 : "+source.length() );
		
		// 压缩蓝图基础代码
		根据图片生成彩色蓝图.output( writer , 根据图片生成彩色蓝图.zip( source.toString() ) );
		writer.close();
		
		System.out.println( "操作结束" );
	}
	
	// 压缩蓝图代码
	private static final ByteArrayOutputStream zip( String source )
	{
		int buffsize = 1024;
		
		byte[] src = source.getBytes();
		System.out.println( "流长度 : "+src.length );
		Deflater zde = new Deflater();
		
		zde.reset();
		zde.setInput( src , 0 , src.length );
		zde.finish();
		
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		byte[] a = new byte[buffsize];
		int length;
		while( ! zde.finished() )
		{
			length = zde.deflate( a , 0 , buffsize );
			s.write( a , 0 , length );
		}
		zde.end();
		
		return s;
	}
	
	// 输出蓝图代码
	private static final void output( BufferedWriter writer , ByteArrayOutputStream s ) throws IOException
	{
		Encoder en = Base64.getEncoder();
		String out = "0" + en.encodeToString( s.toByteArray() );
		
		//System.out.println( "输出结果 : "+out );
		System.out.println( "输出结果长度 : "+out.length() );
		writer.write( out );
		writer.flush();
	}
}
