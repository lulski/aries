"use client";

import { Button, Container, Group, Pagination } from "@mantine/core";
import { getSessionData } from "../lib/sessionUtil";
import PostInline from "../components/PostInline";
import { get } from "http";
import Link from "next/link";
import React from "react";
import { Usable } from "react";
import {
  fetchPaginatedPosts,
  fetchPost,
  PostApiResponse,
} from "../lib/fetchPost";

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
  const unwrappedSearchParams = React.use(searchParams);
  const page = unwrappedSearchParams.page || "1"; // Default to '1' if no page param
  const size = unwrappedSearchParams.size || "10"; // Default to '10' if no size param
  console.log(`page: ${page}, size: ${size}`);

  const data = getPosts(parseInt(page), parseInt(size));

  // TODO: IMPLEMENT PAGINATION
  // const totalPages = data.total / size;

  // // const sessionData = await getSessionData();
  // // console.log("sessionData: " + sessionData);

  // return (
  //   <>
  //     {/* {sessionData.isAuthenticated &&
  //       sessionData.authorities.includes("ADMIN") && (
  //         <Container>
  //           <Group justify="flex-end">
  //             <Button
  //               component="a"
  //               href="/posts/new"
  //               bottom={"10px"}
  //               pos={"relative"}
  //             >
  //               Post New
  //             </Button>
  //           </Group>
  //         </Container>
  //       )} */}
  //     <Container>
  //       <ul>
  //         {posts.map((post, index) => (
  //           <li key={index}>
  //             <PostInline {...post}></PostInline>
  //           </li>
  //         ))}
  //       </ul>
  //       <Pagination
  //         total={totalPages}
  //         value={page}
  //         component={Link}
  //       ></Pagination>
  //     </Container>
  //   </>
  // );
}
