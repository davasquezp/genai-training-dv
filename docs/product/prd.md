# PRD: LatinVibe MVP (Interest + Communities)

## Summary
LatinVibe MVP validates demand by collecting dancer interest and enabling communities to present themselves (landing page + links) with basic constraints (tiers/limits).

## Goals
- Capture dancer interest with minimal friction.
- Allow a community (city/area) to exist with a simple landing page and communication links.
- Provide organizers with basic visibility into registrations and communities.
- Enable monetization for the dancing community via payments (tiered communities and paid offerings over time).

## Monetization (sub-goal)
Build monetization opportunities into the product design while keeping the MVP focused.

### Initial monetization sources
- **Tiered communities**: charge for creating communities with specific features/capabilities in a tiered manner.
- **Commission-based ticketing**: take a commission via payment gateways for event ticketing such as festivals (including accommodation) or workshops.

## Non-goals (MVP)
- Full authentication / accounts
- Matching algorithm
- Messaging/chat
- Payments (implementation)
- Full event scheduling (may be linked as a related feature later)

## Target users
- **Dancers**: want to express interest and preferences (role/styles).
- **Community admins**: want a simple landing page and to centralize links/channels.

## MVP scope

### Dancer interest
- Form captures:
  - name
  - role (lead/follower)
  - country (code + name)
  - styles (multi-select; e.g. Bachata/Salsa/Kizomba/Zouk)
- Admin/debug listing to verify submissions.
- Persistence:
  - in-memory initially (upgrade path to durable storage)

### Communities

#### Community entity
- Has a **name**
- Has a **region** (city/area)
- Has **one or more admins**
- Has a **landing page**
- Can publish **communication links**:
  - WhatsApp
  - Email
  - Telegram
  - Instagram

#### Dancer ↔ community association
- A dancer can belong to **zero or many** communities.

#### Tiered limits
- Communities have tiered limits on how many dancers can belong to them.
- Tier model details (exact tiers/limits/pricing) can be defined later; MVP needs:
  - a way to represent a tier
  - enforcement of a max members value per community

#### Related features (associations)
Communities can be associated with related features such as:
- Events
- Parties
- Schools
- Instructors

MVP requirement is to represent/link these (not to build full modules).

## User stories (lean)
- As a dancer, I can submit my interest with my role and styles.
- As a community admin, I can create a community with region/name and publish my channel links.
- As a dancer, I can be linked to communities I belong to.
- As an admin, I can see submissions and communities in a basic listing to validate data flow.

## Acceptance criteria
- Deployed UI can submit interest successfully to the backend.
- Submissions are retrievable in an admin/debug listing.
- Communities can be created and rendered as a landing page with channel links.
- Tier limit enforcement prevents adding dancers beyond the configured max.

## Metrics
- Registrations per week
- Submit success rate (vs failures)
- Number of communities created
- % of communities with at least 2 links configured

## Dependencies
- Deployed backend and frontend

## Risks & mitigations
- **Deployment misconfiguration** → document and standardize the deployment process.
- **Data loss with in-memory persistence** → prioritize durable persistence soon after MVP validation.
- **Admin ownership/auth** → start with minimal admin concept; add auth in a later milestone.

## Out of scope decisions (defer)
- Pricing and tier names
- Admin authentication model
- Community moderation policies

