package com.praveen.redis.autocomplete;

import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class AutoComplete
{
    public static void main(String[] args) throws IOException
    {
        Jedis jedis = new Jedis("localhost",6379);
//        jedis.auth("");

        Path path = Paths.get("Redis/names.txt");
        List<String> names = Files.readAllLines(path);
        for (String name : names)
        {
            jedis.zadd("autofill",0,name);
        }

        String searchTerm = "b";

        byte[] prefixByte = ("[" + searchTerm).getBytes();
        byte[] prefixByteExtended = Arrays.copyOf(prefixByte, prefixByte.length + 1);
        prefixByteExtended[prefixByte.length] = (byte) 0xFF;
        Set<String> autofill = jedis.zrangeByLex("autofill", "["+searchTerm, new String(prefixByteExtended));

        System.out.println(autofill);
    }
}
