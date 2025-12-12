import AriesPagination from "@/app/components/Pagination/AriesPagination";
import { SessionData } from "@/app/lib/definitions";
import { Button, Group, SimpleGrid } from "@mantine/core";
import { getIronSession } from "iron-session";
import { cookies, headers } from "next/headers";
import PostInline from "../components/Post/PostInline";
import { PostApiResponse } from "../lib/postsApiCall";

type SearchParams = Promise<{ [key: string]: string | string[] | undefined }>;

// export async function generateMetadata(props: { searchParams: SearchParams }) {
//   const searchParams = await props.searchParams;
// }
export async function getBaseUrl() {
  const h = await headers();
  const host = h.get("host");
  const proto = h.get("x-forwarded-proto") ?? "http"; // usually set in AWS

  return `${proto}://${host}`;
}

async function getPosts(
  currentPage: number,
  size: number
): Promise<PostApiResponse> {
  const API_BFF_POST_URL = (await getBaseUrl()) + "/api/posts";
  const fetchUrl = `${API_BFF_POST_URL}?page=${currentPage}&size=${size}`;
  const response = await fetch(fetchUrl);

  if (!response.ok) {
    throw new Error("Failed to fetch posts");
  }
  const responseJson: PostApiResponse = await response.json();

  return responseJson;
}

const SESSION_KEY = process.env.SESSION_KEY;
const COOKIE_NAME = process.env.COOKIE_NAME;

export async function isPostButtonEnabled() {
  const ariescookie = await cookies();
  const session = await getIronSession<SessionData>(ariescookie, {
    password: SESSION_KEY,
    cookieName: COOKIE_NAME,
  });
  console.dir(session, { depth: 5 });
  if (session?.isAuthenticated && session.authorities.includes("ADMIN"))
    return true;
  else return false;
}

export default async function Posts(props: { searchParams: SearchParams }) {
  const searchParams = await props.searchParams;

  const pageNum = Number(searchParams.page ?? "1");
  const sizeNum = Number(searchParams.size ?? "10");
  const posts = await getPosts(pageNum, sizeNum);
  const showNewButton = await isPostButtonEnabled();
  const pagination = {
    total: posts.total ?? 0,
    page: pageNum,
    size: sizeNum,
  };

  // if (error) return <ErrorBanner message={error} onRetry={null} />;

  // if (!posts) return <div>Loading...</div>;
  return (
    <>
      {showNewButton && (
        <SimpleGrid>
          <Group justify="flex-end">
            <Button
              component="a"
              href="/posts/new"
              bottom={"10px"}
              pos={"relative"}
            >
              New Post
            </Button>
          </Group>
        </SimpleGrid>
      )}
      <SimpleGrid>
        {posts &&
          posts.postDto &&
          posts.postDto.map((post, index) => (
            <PostInline {...post} key={index}></PostInline>
          ))}
      </SimpleGrid>

      <Group justify="center">
        {pagination && posts.total > 0 && <AriesPagination {...pagination} />}
      </Group>
    </>
  );
}
