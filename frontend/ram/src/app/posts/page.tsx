"use client";

import { Container } from "@mantine/core";
import { useSearchParams } from "next/navigation";
import { Usable, useEffect, useState } from "react";
import AriesPagination, {
  AriesPaginationProps,
} from "../components/Pagination/AriesPagination";
import PostInline from "../components/PostInline";
import { PostApiResponse } from "../lib/fetchPost";

async function getPosts(
  currentPage: number,
  size: number
): Promise<PostApiResponse> {
  const API_BFF_POST_URL = "/api/posts";
  console.log(
    `Fetching posts from: ${API_BFF_POST_URL}?page=${currentPage}&size=${size}`
  );
  const fetchUrl = `${API_BFF_POST_URL}?page=${currentPage}&size=${size}`;
  const response = await fetch(fetchUrl);

  if (!response.ok) {
    throw new Error("Failed to fetch posts");
  }
  const responseJson: PostApiResponse = await response.json();

  console.log("Fetched posts:", responseJson);
  return responseJson;
}

export default function Posts({
  searchParams,
}: {
  searchParams: Usable<{ page?: string; size?: string }>;
}) {
  const searchParam = useSearchParams();
  const page = searchParam.get("page") || "1";
  const size = searchParam.get("size") || "10";
  console.log(`page: ${page}, size: ${size}`);

  const [posts, setPosts] = useState<PostApiResponse | null>(null);
  const [pagination, setPagination] = useState<AriesPaginationProps | null>(
    null
  );

  useEffect(() => {
    getPosts(parseInt(page), parseInt(size)).then((response) => {
      setPosts(response);

      const pagination: AriesPaginationProps = {
        total: response.total ? response.total : 0,
        page: page ? parseInt(page) : 1,
        size: size ? parseInt(size) : 10,
      };
      setPagination(pagination);
    });
  }, [page, size]);

  if (!posts) return <div>Loading...</div>;

  return (
    <>
      {/* {sessionData.isAuthenticated &&
        sessionData.authorities.includes("ADMIN") && (
          <Container>
            <Group justify="flex-end">
              <Button
                component="a"
                href="/posts/new"
                bottom={"10px"}
                pos={"relative"}
              >
                Post New
              </Button>
            </Group>
          </Container>
        )} */}
      <Container>
        <ul>
          {posts.postDto.map((post, index) => (
            <li key={index}>
              <PostInline {...post}></PostInline>
            </li>
          ))}
        </ul>
        {pagination && posts.total > 0 && <AriesPagination {...pagination} />}
      </Container>
    </>
  );
}
