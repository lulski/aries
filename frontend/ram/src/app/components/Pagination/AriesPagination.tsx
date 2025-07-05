"use client";
import { Pagination } from "@mantine/core";

export interface AriesPaginationProps {
  total: number;
  page: number;
  size: number;
}

export default function AriesPagination({
  total,
  page,
  size,
}: AriesPaginationProps) {
  return (
    <Pagination
      total={Math.ceil(total / size)}
      value={page}
      size={size}
      // component={Link}
    ></Pagination>
  );
}
