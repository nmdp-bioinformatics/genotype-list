# Introduction #

To demonstrate the genotype list service, we have made two AMIs available -- one with preloaded IMGT/HLA data, and one without any preloaded data.


# Details #

  * Finding the AMIs

> The preloaded and data-free AMIs are available in both East and West (Oregon) regions of Amazon.
> <ul>
<blockquote><li>West/Oregon<br>
<blockquote><ul>
<blockquote><li>ami-57069467 includes preloaded IMGT/HLA data</li>
<li>ami-cf0193ff does not include preloaded IMGT/HLA data.</li>
</blockquote></ul>
</blockquote></li>
<li>East/Virginia<br>
<blockquote><ul>
<blockquote><li>ami-54b7f03d includes preloaded IMGT/HLA data</li>
<li>ami-24b7f04d does not include preloaded IMGT/HLA data.</li>
</blockquote></ul>
</blockquote></li>
</ul></blockquote>

# Using the AMIs #

<ol>
<li>Before Launching the AMI<br>
<ol>
<blockquote><li>get yourself an elastic IP address.  Write it down.</li>
<li>get a DNSname to refer to the elastic IP address.  Write that down, you'll need it soon.</li>
</ol>
</li>
<li>Launching the AMI<br>
Launch a new machine using the AMI, but from the quick-launch wizard, you'll want to do a few things<br>
<ol>
<blockquote><li>set your namespace in the "user details" section, using the name you got up in step #2 of "before.  You'll want to set it thusly:<br>
NAMESPACE=domainname.that.points.to.your.public.ip</li>
<li>Set your security group to allow inbound ssh and http traffic.  If you want https traffic, we recommend you put your certificate on an Elastic Load Balancer, and terminate the SSL traffic there.  Contact Jeremy Anderson, janders3@nmdp.org, for more information on that.</li>
</ol>
</li>
<li>Using the AMI<br>
</blockquote></blockquote><blockquote>Give your new machine a few minutes to start up, and for the scripts to automatically adjust the namespace in the application.  Then, point your browser to <a href='http://domainname.that.points.to.your.public.ip/'>http://domainname.that.points.to.your.public.ip/</a>  -- or whatever you actually put in the user-details section above.<br>
</li>
</ol></blockquote>

What size should you use?  We're working on that.  I suspect a micro will be a bit underpowered for you, but on the other hand, if it's your only AWS EC2 machine, it's free.  We'll be adding performance data soon.