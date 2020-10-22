import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Vowel_words
{

        public static class TokenizerMapper
        extends Mapper<Object, Text, Text, Text>
        {
    private Text vword = new Text();
    private Text v = new Text();
    private char fc;
    private String word="";
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException
    {
      StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens())
      {
        word=itr.nextToken();
        fc=Character.toUpperCase(word.charAt(0));
        if(fc=='A' || fc=='E' ||fc=='I'||fc=='O'||fc=='U')
        {
            v.set(Character.toString(fc));
            vword.set(word);
            context.write(v,vword);
        }
    }
  }
  }

  public static class Appendz
       extends Reducer<Text,Text,Text,Text>
       {
         private Text output = new Text();
         public void reduce(Text key, Iterable<Text> values,Context context) throws IOException, InterruptedException
        {
      String wordarray="";
      for(Text val:values)
      {
          wordarray=wordarray+val.toString()+",";

      }
      output.set(wordarray);
      context.write(key,output);
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Vowel_words");
    job.setJarByClass(Vowel_words.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(Appendz.class);
    job.setReducerClass(Appendz.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
