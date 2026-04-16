# MVP scope

## MVP goal
Validate that dancers will register interest, communities can present themselves, and organizers can get actionable insights.

## In scope (MVP)
- Public landing page + Interest form
- Capture: name, role (lead/follower), country, styles (multi-select)
- Persist registrations (currently in-memory; next step is durable storage)
- Admin/debug listing endpoint (`GET /api/dancers`) for verification
- Communities (concept + initial slice)
  - A community has a **name**
  - A community is based in a particular **region** (city or area)
  - A community has **one or more admins**
  - A dancer can be associated with **zero or many** communities
  - Tiered limits define how many dancers can belong to a community
  - A community has a **landing page**
  - A community can be associated with related features such as **Events**, **Parties**, **Schools**, or **Instructors**
  - A community can publish several links for communication channels:
    - WhatsApp
    - Email
    - Telegram
    - Instagram
- Basic deployability:
  - backend on Render
  - frontend on Cloudflare Pages

## Non-goals (MVP)
- Authentication / accounts
- Matching algorithm
- Messaging / chat
- Payments (implementation)
- Event scheduling

## Release criteria
- A dancer can submit their interest and receives a clear confirmation that it was received.
- Organizers can view registrations for a community/region to validate demand.
- A community has a public landing page that shows its name, region, and communication links.
- A community cannot exceed its configured tier limit for members.

