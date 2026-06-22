import type { Meta, StoryObj } from "@storybook/nextjs-vite";

import { expect, within } from "storybook/test";

import { PostData } from "../../lib/definitions";
import Post from "./Post";

const samplePost: PostData = {
  id: "1",
  title: "Hello World",
  titleUrl: "hello-world",
  content: `
            <h1>This is h1</h1>
            <h2>This is h2</h2>
            <h3>This is h3</h3>
            <h4>This is h4</h4>
            <h5>This is h5</h5>
            <h6>This is h6</h6>
            <b>This is bold content</b><br/>
            <i>This is italic content</i>
            <p>This is <strong>post</strong> content</p>
            <ul>
              <li>First item</li>
              <li>Second item</li>
              <li>Third item</li>
            </ul>
            <ol>
              <li>First ordered item</li>
              <li>Second ordered item</li>
              <li>Third ordered item</li>
            </ol>
            <a href="https://example.com">This is a link</a>
            `,
  author: "Alice",
  createdOn: "2026-03-08",
  modifiedOn: "2026-03-08",
};

const meta = {
  title: "Components/Post",
  component: Post,
  tags: ["autodocs"],
  args: {
    post: samplePost,
    allowHtmlMarkup: true,
  },
} satisfies Meta<typeof Post>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {};

// interaction test ensures basic fields render correctly
Default.play = async ({ canvasElement }) => {
  const canvas = within(canvasElement);
  // title should be visible
  await expect(
    canvas.getByRole("heading", { name: /hello world/i }),
  ).toBeInTheDocument();
  // content from the editor should render
  await expect(canvas.getByText(/this is post content/i)).toBeInTheDocument();
  // author and date appear
  await expect(canvas.getByText(/alice/i)).toBeInTheDocument();
  await expect(canvas.getByText(/2026-03-08/)).toBeInTheDocument();
};
