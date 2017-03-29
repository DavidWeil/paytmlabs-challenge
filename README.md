# paytmlabs-challenge
Challenge exercise for PaytmLabs

## Technology decisions:
It was fairly obvious that I needed a container-based solution, and an as-close-to-stateless as
possible server.  I wasn't able to get Kubernetes running stably on my machine, so that left Docker
as a container platform.  Building a set of individual REST-type servlets allows for their deployments
to scale independently of one another.  However, to avoid having to build a reverse proxy (and bundle
NGINX or similar tool), I opted to package all the wars in a single docker image instead of building
separate docker images for each servlet.

I opted for the LCBO option because last time I played with the Twitter API the results of searching
for tweets was disappointing.  At least with the LCBO there's always data available, and testing
is not affected by what other people are talking about.

(I admit that when I made that decision I hadn't realised that the LCBO API didn't provide a search
function, which would have been the true winner for choosing it over the others.)

I used IDEA as a platform out of familiarity, Gradle as a build system for its flexibility and the
wide set of available extensions (like building docker containers).  GSON is fairly performant while
also being quite flexible (I used that to specify custom deserializers for all the returned JSON).
Java because I'm most comfortable in it, though I did briefly consider Scala.  (Case classes work
very well for deserializing data into.)

I did build the "hello world" example using Jersey (JAX-RS/JSR 311) to simplify the description of
the REST interface, but found that it did not work well with unit testing.  (It may simply have been
that IDEA did not want to play well with Jetty, but I didn't want to spend the time trying to debug
it.)  Obviously something like the separation of methods according to media type would have been
nice, but I opted to continue with the challenge using basic servlets instead of investigating
a different JAX-RS implementation.

Rather than package a database and build the access service, I opted (for simplicity) to store user
data locally in the User servlet.  That means that "persistence" is purely theoretical for now, but
data will persist while the application is running.

## Post Mortem
I spent too much time fiddling with tools:  Kubernetes/Docker when I should have been simply
deploying apps into a local tomcat instance for development, Jersey when I didn't know it well enough
to understand its limitations.

I also had overly ambitious plans; for example in stage 3 I was planning on including selecting a
preferred store and reporting inventory levels at that location.

## Running the App
The application - such as it is - works, at least.  Build the docker image for the product module
and deploy it to your docker container.  It's a tomcat-based image, so it's exposing 8080.
Connecting to `http://{container external URL/port}/product` will show a paged list of LCBO products.
Selecting a product from the list will take you to a page with some details of that product.

## Future thoughts
In addition to more features in the base app:
- selected store was to be passed back as a cookie in addition to being stored in the user, so the
  selection would follow the user around without needing fast replication.
- I was planning on adding a "featured product" inset to all pages based on a random choice.  (I
  couldn't find anything that specifically retrieved sale products.)
- I had some half-formed ideas for performing searches by pulling product data since the API lacks
  any search features.
- Using the store API and the inventory queries, I was going to add a suggestion of an alternative
  nearby store to purchase a product at if it was not available at the user's selected store.

## Bonus
The idea is probably a bit half-baked, but an array of 256 shorts would create 4096 hash buckets
which map to the last 3 hex digits of a product hash, while still only occupying 512 bytes.  There
will be a high potential for false matches, but the test will be fairly cheap, and it satisfies
the "no repeats" criterion.  If the user visits > 2000 products, I would reprocess their last 1000
visits after they log out in the theory that they won't remember whether they've seen something that
last popped up more than a 1000 pages previously.
