package com.jeremy.estiam.appliandroid.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class NoSSLv3SocketFactory
  extends SSLSocketFactory
{
  private final SSLSocketFactory delegate;
  
  public NoSSLv3SocketFactory()
  {
    this.delegate = HttpsURLConnection.getDefaultSSLSocketFactory();
  }
  
  public NoSSLv3SocketFactory(SSLSocketFactory delegate)
  {
    this.delegate = delegate;
  }
  
  public String[] getDefaultCipherSuites()
  {
    return this.delegate.getDefaultCipherSuites();
  }
  
  public String[] getSupportedCipherSuites()
  {
    return this.delegate.getSupportedCipherSuites();
  }
  
  private Socket makeSocketSafe(Socket socket)
  {
    if ((socket instanceof SSLSocket)) {
      socket = new NoSSLv3SSLSocket((SSLSocket)socket);
    }
    return socket;
  }
  
  public Socket createSocket(Socket s, String host, int port, boolean autoClose)
    throws IOException
  {
    return makeSocketSafe(this.delegate.createSocket(s, host, port, autoClose));
  }
  
  public Socket createSocket(String host, int port)
    throws IOException
  {
    return makeSocketSafe(this.delegate.createSocket(host, port));
  }
  
  public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
    throws IOException
  {
    return makeSocketSafe(this.delegate.createSocket(host, port, localHost, localPort));
  }
  
  public Socket createSocket(InetAddress host, int port)
    throws IOException
  {
    return makeSocketSafe(this.delegate.createSocket(host, port));
  }
  
  public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
    throws IOException
  {
    return makeSocketSafe(this.delegate.createSocket(address, port, localAddress, localPort));
  }
  
  private class NoSSLv3SSLSocket
    extends NoSSLv3SocketFactory.DelegateSSLSocket
  {
    private NoSSLv3SSLSocket(SSLSocket delegate)
    {
      super(delegate);
    }
    
    public void setEnabledProtocols(String[] protocols)
    {
      if ((protocols != null) && (protocols.length == 1) && ("SSLv3".equals(protocols[0])))
      {
        List<String> enabledProtocols = new ArrayList(Arrays.asList(this.delegate.getEnabledProtocols()));
        if (enabledProtocols.size() > 1)
        {
          enabledProtocols.remove("SSLv3");
          System.out.println("Removed SSLv3 from enabled protocols");
        }
        else
        {
          System.out.println("SSL stuck with protocol available for " + String.valueOf(enabledProtocols));
        }
        protocols = (String[])enabledProtocols.toArray(new String[enabledProtocols.size()]);
      }
      super.setEnabledProtocols(protocols);
    }
  }
  
  public class DelegateSSLSocket
    extends SSLSocket
  {
    protected final SSLSocket delegate;
    
    DelegateSSLSocket(SSLSocket delegate)
    {
      this.delegate = delegate;
    }
    
    public String[] getSupportedCipherSuites()
    {
      return this.delegate.getSupportedCipherSuites();
    }
    
    public String[] getEnabledCipherSuites()
    {
      return this.delegate.getEnabledCipherSuites();
    }
    
    public void setEnabledCipherSuites(String[] suites)
    {
      this.delegate.setEnabledCipherSuites(suites);
    }
    
    public String[] getSupportedProtocols()
    {
      return this.delegate.getSupportedProtocols();
    }
    
    public String[] getEnabledProtocols()
    {
      return this.delegate.getEnabledProtocols();
    }
    
    public void setEnabledProtocols(String[] protocols)
    {
      this.delegate.setEnabledProtocols(protocols);
    }
    
    public SSLSession getSession()
    {
      return this.delegate.getSession();
    }
    
    public void addHandshakeCompletedListener(HandshakeCompletedListener listener)
    {
      this.delegate.addHandshakeCompletedListener(listener);
    }
    
    public void removeHandshakeCompletedListener(HandshakeCompletedListener listener)
    {
      this.delegate.removeHandshakeCompletedListener(listener);
    }
    
    public void startHandshake()
      throws IOException
    {
      this.delegate.startHandshake();
    }
    
    public void setUseClientMode(boolean mode)
    {
      this.delegate.setUseClientMode(mode);
    }
    
    public boolean getUseClientMode()
    {
      return this.delegate.getUseClientMode();
    }
    
    public void setNeedClientAuth(boolean need)
    {
      this.delegate.setNeedClientAuth(need);
    }
    
    public void setWantClientAuth(boolean want)
    {
      this.delegate.setWantClientAuth(want);
    }
    
    public boolean getNeedClientAuth()
    {
      return this.delegate.getNeedClientAuth();
    }
    
    public boolean getWantClientAuth()
    {
      return this.delegate.getWantClientAuth();
    }
    
    public void setEnableSessionCreation(boolean flag)
    {
      this.delegate.setEnableSessionCreation(flag);
    }
    
    public boolean getEnableSessionCreation()
    {
      return this.delegate.getEnableSessionCreation();
    }
    
    public void bind(SocketAddress localAddr)
      throws IOException
    {
      this.delegate.bind(localAddr);
    }
    
    public synchronized void close()
      throws IOException
    {
      this.delegate.close();
    }
    
    public void connect(SocketAddress remoteAddr)
      throws IOException
    {
      this.delegate.connect(remoteAddr);
    }
    
    public void connect(SocketAddress remoteAddr, int timeout)
      throws IOException
    {
      this.delegate.connect(remoteAddr, timeout);
    }
    
    public SocketChannel getChannel()
    {
      return this.delegate.getChannel();
    }
    
    public InetAddress getInetAddress()
    {
      return this.delegate.getInetAddress();
    }
    
    public InputStream getInputStream()
      throws IOException
    {
      return this.delegate.getInputStream();
    }
    
    public boolean getKeepAlive()
      throws SocketException
    {
      return this.delegate.getKeepAlive();
    }
    
    public InetAddress getLocalAddress()
    {
      return this.delegate.getLocalAddress();
    }
    
    public int getLocalPort()
    {
      return this.delegate.getLocalPort();
    }
    
    public SocketAddress getLocalSocketAddress()
    {
      return this.delegate.getLocalSocketAddress();
    }
    
    public boolean getOOBInline()
      throws SocketException
    {
      return this.delegate.getOOBInline();
    }
    
    public OutputStream getOutputStream()
      throws IOException
    {
      return this.delegate.getOutputStream();
    }
    
    public int getPort()
    {
      return this.delegate.getPort();
    }
    
    public synchronized int getReceiveBufferSize()
      throws SocketException
    {
      return this.delegate.getReceiveBufferSize();
    }
    
    public SocketAddress getRemoteSocketAddress()
    {
      return this.delegate.getRemoteSocketAddress();
    }
    
    public boolean getReuseAddress()
      throws SocketException
    {
      return this.delegate.getReuseAddress();
    }
    
    public synchronized int getSendBufferSize()
      throws SocketException
    {
      return this.delegate.getSendBufferSize();
    }
    
    public int getSoLinger()
      throws SocketException
    {
      return this.delegate.getSoLinger();
    }
    
    public synchronized int getSoTimeout()
      throws SocketException
    {
      return this.delegate.getSoTimeout();
    }
    
    public boolean getTcpNoDelay()
      throws SocketException
    {
      return this.delegate.getTcpNoDelay();
    }
    
    public int getTrafficClass()
      throws SocketException
    {
      return this.delegate.getTrafficClass();
    }
    
    public boolean isBound()
    {
      return this.delegate.isBound();
    }
    
    public boolean isClosed()
    {
      return this.delegate.isClosed();
    }
    
    public boolean isConnected()
    {
      return this.delegate.isConnected();
    }
    
    public boolean isInputShutdown()
    {
      return this.delegate.isInputShutdown();
    }
    
    public boolean isOutputShutdown()
    {
      return this.delegate.isOutputShutdown();
    }
    
    public void sendUrgentData(int value)
      throws IOException
    {
      this.delegate.sendUrgentData(value);
    }
    
    public void setKeepAlive(boolean keepAlive)
      throws SocketException
    {
      this.delegate.setKeepAlive(keepAlive);
    }
    
    public void setOOBInline(boolean oobinline)
      throws SocketException
    {
      this.delegate.setOOBInline(oobinline);
    }
    
    public void setPerformancePreferences(int connectionTime, int latency, int bandwidth)
    {
      this.delegate.setPerformancePreferences(connectionTime, latency, bandwidth);
    }
    
    public synchronized void setReceiveBufferSize(int size)
      throws SocketException
    {
      this.delegate.setReceiveBufferSize(size);
    }
    
    public void setReuseAddress(boolean reuse)
      throws SocketException
    {
      this.delegate.setReuseAddress(reuse);
    }
    
    public synchronized void setSendBufferSize(int size)
      throws SocketException
    {
      this.delegate.setSendBufferSize(size);
    }
    
    public void setSoLinger(boolean on, int timeout)
      throws SocketException
    {
      this.delegate.setSoLinger(on, timeout);
    }
    
    public synchronized void setSoTimeout(int timeout)
      throws SocketException
    {
      this.delegate.setSoTimeout(timeout);
    }
    
    public void setTcpNoDelay(boolean on)
      throws SocketException
    {
      this.delegate.setTcpNoDelay(on);
    }
    
    public void setTrafficClass(int value)
      throws SocketException
    {
      this.delegate.setTrafficClass(value);
    }
    
    public void shutdownInput()
      throws IOException
    {
      this.delegate.shutdownInput();
    }
    
    public void shutdownOutput()
      throws IOException
    {
      this.delegate.shutdownOutput();
    }
    
    public String toString()
    {
      return this.delegate.toString();
    }
    
    public boolean equals(Object o)
    {
      return this.delegate.equals(o);
    }
  }
}
